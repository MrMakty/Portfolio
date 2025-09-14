import { Component, inject, OnInit } from '@angular/core';
import { Observable, forkJoin } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Profile } from '../models/profile';
import { ProfileService } from '../services/profile.service';
import { Router } from '@angular/router';
import { DockService } from '../services/dock.service';
import { ReservationService } from '../services/reservation.service';
import { Dock } from '../models/dock';
import { Reservation } from '../models/reservation';
import {FormsModule} from '@angular/forms';
import {TranslatePipe} from '@ngx-translate/core';

@Component({
  selector: 'app-admin',
  standalone: true,
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.scss',
  imports: [
    FormsModule,
    TranslatePipe
  ]
})
export class AdminComponent implements OnInit {
  private profileService = inject(ProfileService);
  private router = inject(Router);
  private dockService = inject(DockService);
  private reservationService = inject(ReservationService);

  dockId: number | null = null;
  dockStatus: Dock | null = null;
  message: string | null = null;

  ngOnInit() {
    this.adminCheck();
  }

  protected adminCheck(): void {
    this.profileService.getUser().pipe(
      tap(user => {
        if (user.role.name !== 'ROLE_ADMIN') {
          console.log('user mag niet naar admin paneel');
          this.router.navigate(['/profile']);
        }
      })
    ).subscribe();
  }

  fetchDockStatus(): void {
    if (this.dockId === null) return;

    this.dockService.getAllDocks().subscribe({
      next: docks => {
        const found = docks.find(d => d.id === this.dockId);
        if (found) {
          this.dockStatus = found;
          this.message = `Dock ${found.id} is momenteel ${found.inMaintenance ? 'in maintenance' : 'beschikbaar'}.`;
        } else {
          this.dockStatus = null;
          this.message = `Dock met ID ${this.dockId} niet gevonden.`;
        }
      },
      error: err => {
        console.error(err);
        this.message = `Fout bij ophalen van dock ${this.dockId}.`;
      }
    });
  }

  toggleMaintenance(): void {
    if (this.dockId === null) return;

    const newStatus = !(this.dockStatus?.inMaintenance ?? false);
    this.dockService.updateMaintenanceStatus(this.dockId, newStatus).subscribe({
      next: () => {
        this.dockStatus = { ...(this.dockStatus as Dock), inMaintenance: newStatus };
        this.message = `Dock ${this.dockId} is nu ${newStatus ? 'op' : 'van'} maintenance gezet.`;
      },
      error: err => {
        console.error(err);
        this.message = `Fout bij updaten van maintenance status.`;
      }
    });
  }

  cancelAllReservations(): void {
    if (this.dockId === null) {
      this.message = 'Voer eerst een geldig Dock ID in.';
      return;
    }

    this.dockService.getReservationsForDock(this.dockId).subscribe({
      next: reservations => {
        if (reservations.length === 0) {
          this.message = `Er zijn geen actieve reserveringen voor dock ${this.dockId}.`;
          return;
        }

        const cancelCalls = reservations.map((r: Reservation) =>
          this.reservationService.cancelReservation(r.id)
        );

        forkJoin(cancelCalls).subscribe({
          next: () => {
            this.message = `Alle ${reservations.length} reserveringen voor dock ${this.dockId} zijn geannuleerd.`;
          },
          error: err => {
            console.error(err);
            this.message = `Er ging iets mis bij het annuleren van reserveringen voor dock ${this.dockId}.`;
          }
        });
      },
      error: err => {
        console.error(err);
        this.message = `Fout bij ophalen reserveringen voor dock ${this.dockId}.`;
      }
    });
  }
}
