import {Component, inject, OnInit} from '@angular/core';
import { NgClass, NgForOf, NgIf, NgStyle } from '@angular/common';
import { Router } from '@angular/router';
import { DockService } from '../../services/dock.service';
import { DockStatusDTO } from '../../dto/DockStatusDTO';
import {FormsModule} from '@angular/forms';
import {Spaceship} from '../../models/spaceship';
import {SpaceshipService} from '../../services/spaceship.service';
import {Observable, tap} from 'rxjs';
import {Profile} from '../../models/profile';
import {ProfileService} from '../../services/profile.service';
import {TranslatePipe} from '@ngx-translate/core';

type DockStatus = 'free' | 'reserved' | 'maintenance';

interface Dock {
  id: number;
  size: 'S' | 'M' | 'L';
  angle: number;
  level: number;
  status: DockStatus;
  reservationEnd?: Date;
}

@Component({
  selector: 'app-docking-map',
  templateUrl: './docking-map.component.html',
  styleUrls: ['./docking-map.component.scss'],
  standalone: true,
  imports: [NgStyle, NgIf, NgForOf, NgClass, FormsModule, TranslatePipe]
})
export class DockingMapComponent implements OnInit {
  selectedLevel = 1;
  docks: Dock[] = [];
  spaceships: Spaceship[] = []; // Voeg deze eigenschap toe
  selectedSpaceship: Spaceship | null = null;
  private profileService = inject(ProfileService);
  protected isAdmin: boolean = false;




  constructor(
    private dockService: DockService,
    private spaceshipService: SpaceshipService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.generateDocks();
    this.loadDockStatuses();
    this.loadSpaceships();
    this.isAdminCheck();
  }

  protected isAdminCheck(): void {
    const user$: Observable<Profile> = this.profileService.getUser();
    user$.pipe(
      tap(user => {
        if (user.role.name === 'ROLE_ADMIN') {
          console.log(user);
          console.log("user is admin");
          this.isAdmin = true;
        } else{
          console.log("user geen admin");
          this.isAdmin = false;
        }
      })
    ).subscribe();
  }

  loadSpaceships(): void {
    this.spaceshipService.getAllSpaceships().subscribe({
      next: (spaceships) => {
        this.spaceships = spaceships; // Sla de schepen op
        console.log(spaceships)
      },
      error: (err) => {
        console.warn('Fout bij het ophalen van schepen:', err);
      }
    });
  }



  generateDocks(): void {
    const level1 = Array.from({ length: 12 }, (_, i) => ({
      id: i + 1,
      size: this.getSizeByIndex(i),
      angle: i * 30,
      level: 1,
      status: 'free' as DockStatus,
    }));

    const level2 = Array.from({ length: 12 }, (_, i) => ({
      id: i + 13,
      size: this.getSizeByIndex(i),
      angle: i * 30,
      level: 2,
      status: 'free' as DockStatus,
    }));

    this.docks = [...level1, ...level2];
  }

  getSizeByIndex(index: number): 'S' | 'M' | 'L' {
    return ['S', 'M', 'L'][index % 3] as 'S' | 'M' | 'L';
  }

  switchLevel(level: number): void {
    this.selectedLevel = level;
  }

  getPolarPosition(angle: number, radius: number = 160): { top: string; left: string } {
    const rad = (angle * Math.PI) / 180;
    const center = 200;
    return {
      top: `${center + Math.sin(rad) * radius}px`,
      left: `${center + Math.cos(rad) * radius}px`
    };
  }

  loadDockStatuses(): void {
    for (const dock of this.docks) {
      this.dockService.getDockStatus(dock.id).subscribe({
        next: (dto: DockStatusDTO) => {
          switch (dto.status) {
            case 'AVAILABLE':
              this.setDockFree(dock.id, dto.availableFrom ? new Date(dto.availableFrom) : new Date());
              break;
            case 'RESERVED':
              this.setDockReserved(dock.id, dto.reservedUntil ? new Date(dto.reservedUntil) : new Date());
              break;
            case 'MAINTENANCE':
              this.setDockMaintenance(dock.id);
              break;
          }
        },
        error: (err) => {
          console.warn(`Fout bij ophalen status voor dock ${dock.id}:`, err);
        }
      });
    }
  }

  setDockFree(dockId: number, availableFrom: Date): void {
    const dock = this.docks.find(d => d.id === dockId);
    if (dock) {
      dock.status = 'free';
      dock.reservationEnd = availableFrom;
    }
  }

  setDockReserved(dockId: number, reservedUntil: Date): void {
    const dock = this.docks.find(d => d.id === dockId);
    if (dock) {
      dock.status = 'reserved';
      dock.reservationEnd = reservedUntil;
    }
  }

  setDockMaintenance(dockId: number): void {
    const dock = this.docks.find(d => d.id === dockId);
    if (dock) {
      dock.status = 'maintenance';
      dock.reservationEnd = undefined;
    }
  }

  getBurenVanDock(dockId: number): Dock[] {
    const index = this.docks.findIndex(d => d.id === dockId);
    if (index === -1) return [];

    const dock = this.docks[index];
    const levelStart = dock.level === 1 ? 1 : 13;
    const levelEnd = dock.level === 1 ? 12 : 24;

    // Wrap-around buren (bijv. naast 1 is 12 en 2)
    const leftId = dock.id === levelStart ? levelEnd : dock.id - 1;
    const rightId = dock.id === levelEnd ? levelStart : dock.id + 1;

    return this.docks.filter(d => d.id === leftId || d.id === rightId);
  }


  openModal(dock: Dock): void {

    if (!this.selectedSpaceship) {
      alert('Selecteer eerst een schip voordat je een dock kiest.');
      return;
    }

    if (this.isAdmin) {
      this.router.navigate([
        '/reserve',
        dock.id,
        'spaceship',
        this.selectedSpaceship!.shipId
      ]);
    }


    // Dock is niet vrij
    if (dock.status !== 'free') {
      if (dock.reservationEnd) {
        alert(`Dock ${dock.id} is bezet tot ${dock.reservationEnd.toLocaleString()}.`);
      } else {
        alert(`Dock ${dock.id} is momenteel niet beschikbaar.`);
      }
      return;
    }

    // Gevaarlijke schepen moeten geen gereserveerde buren hebben
    if (this.selectedSpaceship.shipType === 'TRADER_DANGEROUS') {
      const buurDocks = this.getBurenVanDock(dock.id);
      const conflict = buurDocks.some(d => d.status !== 'free');
      if (conflict) {
        alert('Er zijn geen vrije plekken naast je. Je bent een chemisch schip en hebt vrije buren nodig.');
        return;
      }
    }

    // Controleer of het formaat van het schip overeenkomt met de dock
    if (this.selectedSpaceship.size.toUpperCase() !== dock.size) {
      alert(`Fout: je schip is formaat ${this.selectedSpaceship.size}, maar deze dock ondersteunt formaat ${dock.size}.`);
      return;
    }

    // Andere schepen mogen niet naast een gevaarlijk schip liggen
    if (this.selectedSpaceship.shipType !== 'TRADER_DANGEROUS') {
      const buurDocks = this.getBurenVanDock(dock.id);
      const naastGevaarlijk = buurDocks.some(d => {
        const dockInfo = this.docks.find(x => x.id === d.id);
        if (!dockInfo || dockInfo.status !== 'reserved') return false;
        // Je zou hier logica kunnen toevoegen om te bepalen welk schip op die plek ligt
        // Voor nu simuleren we dat met dock.status alleen
        return true; // Stel dat elk gereserveerd dock een gevaarlijk schip bevat (placeholder)
      });

      if (naastGevaarlijk) {
        alert('Je mag niet naast een TRADER_DANGEROUS schip liggen. Kies een andere plek.');
        return;
      }
    }

    console.log(this.selectedSpaceship.shipId)
    console.log(this.selectedSpaceship.shipId)
    console.log(this.selectedSpaceship.shipId)

    // Alles ok, navigeren naar reserveringspagina
    this.router.navigate([
      '/reserve',
      dock.id,
      'spaceship',
      this.selectedSpaceship!.shipId
    ]);
  }

}
