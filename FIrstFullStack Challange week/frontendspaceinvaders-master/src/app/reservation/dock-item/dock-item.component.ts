import { Component, OnInit } from '@angular/core';
import { DockService } from '../../services/dock.service';
import { ReservationService } from '../../services/reservation.service';
import { Reservation } from '../../models/reservation';
import { addHours, startOfDay, endOfDay, eachDayOfInterval, addDays, isBefore } from 'date-fns';
import {ActivatedRoute, Router} from '@angular/router';
import { DatePipe, NgClass, NgForOf, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CreateReservationDTO } from '../../dto/CreateReservationDTO';
import {TranslatePipe} from '@ngx-translate/core';

@Component({
  selector: 'app-dock-item',
  templateUrl: './dock-item.component.html',
  imports: [
    NgIf,
    DatePipe,
    FormsModule,
    NgForOf,
    NgClass,
    TranslatePipe
  ],
  styleUrls: ['./dock-item.component.scss']
})
export class DockItemComponent implements OnInit {
  dockId!: number;
  spaceshipId!: number;
  status: 'AVAILABLE' | 'RESERVED' | 'MAINTENANCE' | null = null;
  availableFrom: Date | null = null;
  reservedUntil: Date | null = null;
  reservations: Reservation[] = [];
  reservationStart: string = '';
  durationHours: number = 1;

  // Voor de tijdlijn
  displayedDays: Date[] = [];
  currentStartDate: Date = new Date();
  selectedSlot: Date | null = null; // Geselecteerde tijdslot
  minDate: string = new Date().toISOString().slice(0, 16); // Minimum datum voor input

  constructor(
    private route: ActivatedRoute,
    private dockService: DockService,
    private reservationService: ReservationService,
    private router: Router

) {}

  ngOnInit(): void {
    const dockIdParam = this.route.snapshot.paramMap.get('dockId');
    const shipIdParam = this.route.snapshot.paramMap.get('shipId');

    if (dockIdParam && shipIdParam) {
      this.dockId = +dockIdParam;
      this.spaceshipId = +shipIdParam;

      this.loadReservations();
      this.updateDisplayedDays();
    } else {
      console.error('dockId en/of shipId ontbreekt in de URL.');
    }
  }


  loadReservations(): void {
    this.dockService.getReservationsForDock(this.dockId).subscribe({
      next: (res) => {
        this.reservations = res.map(r => ({
          ...r,
          startTimestamp: new Date(r.startTimestamp),
          endTimestamp: new Date(r.endTimestamp)
        }));
      },
      error: (err) => {
        console.error(`Fout bij ophalen reserveringen voor dock ${this.dockId}:`, err);
      }
    });
  }

  updateDisplayedDays(): void {
    this.displayedDays = eachDayOfInterval({
      start: startOfDay(this.currentStartDate),
      end: endOfDay(addDays(this.currentStartDate, 2))
    });
  }

  changeDays(direction: number): void {
    this.currentStartDate = addDays(this.currentStartDate, direction);
    this.updateDisplayedDays();
  }

  getTimeSlots(day: Date): Date[] {
    const slots: Date[] = [];
    const startOfDayDate = startOfDay(day);
    for (let hour = 0; hour < 24; hour++) {
      slots.push(addHours(startOfDayDate, hour));
    }
    return slots;
  }

  isReserved(slot: Date): boolean {
    return this.reservations.some(r =>
      slot >= r.startTimestamp && slot < r.endTimestamp
    );
  }

  isFuture(slot: Date): boolean {
    return isBefore(new Date(), slot);
  }

  selectTimeSlot(slot: Date): void {
    if (!this.isReserved(slot) && this.isFuture(slot)) {
      this.selectedSlot = slot;
      this.reservationStart = slot.toISOString().slice(0, 16);
      this.durationHours = 1;
    }
  }

  confirmReservation(): void {
    if (!this.selectedSlot) {
      alert('Selecteer een tijdslot.');
      return;
    }
    this.submitReservation();
  }

  submitReservation(): void {
    if (!this.reservationStart || this.durationHours < 1 || this.durationHours > 24) {
      alert('Ongeldige invoer.');
      return;
    }

    const start = addHours(new Date(this.reservationStart), 2);
    const end = new Date(start.getTime() + this.durationHours * 60 * 60 * 1000); // Duur in milliseconden

    // Controleer of de gekozen tijd niet overlapt met bestaande reserveringen
    if (!this.canReserve(start, end)) {
      alert('De gekozen tijd overlapt met een bestaande reservering.');
      return;
    }

    // Maak de reservering aan met de juiste DTO
    const reservationDTO: CreateReservationDTO = {
      dockId: this.dockId,
      spaceshipId: this.spaceshipId, // Dit moet mogelijk dynamisch zijn, afhankelijk van de gebruiker
      startTimestamp: start , // Dit is een Date object
      endTimestamp: end // Dit is ook een Date object
    };

    this.reservationService.createReservation(reservationDTO).subscribe({
      next: () => {
        alert('Reservering succesvol aangemaakt.');
        this.loadReservations(); // Laad de reserveringen opnieuw na succesvolle reservering
        this.selectedSlot = null; // Reset de geselecteerde slot na succesvolle reservering
        this.router.navigate(['']);
      },
      error: (err) => {
        console.error('Fout bij het aanmaken van de reservering:', err);
        alert('Reservering mislukt.');
      }
    });
  }

  canReserve(start: Date, end: Date): boolean {
    return !this.reservations.some(r =>
      (start < r.endTimestamp && end > r.startTimestamp)
    );
  }

  isFirstDay(): boolean {
    return this.currentStartDate <= new Date();
  }
}
