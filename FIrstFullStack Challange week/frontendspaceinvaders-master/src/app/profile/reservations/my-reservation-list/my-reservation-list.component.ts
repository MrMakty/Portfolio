import {Component, inject, OnInit} from '@angular/core';
import {MyReservationItemComponent} from '../my-reservation-item/my-reservation-item.component';
import {Reservation} from '../../../models/reservation';
import {ReservationService} from '../../../services/reservation.service';
import {NgForOf} from '@angular/common';
import {TranslatePipe} from '@ngx-translate/core';

@Component({
  selector: 'app-my-reservation-list',
  imports: [
    MyReservationItemComponent,
    NgForOf,
    TranslatePipe
  ],
  templateUrl: './my-reservation-list.component.html',
  standalone: true,
  styleUrl: './my-reservation-list.component.scss'
})
export class MyReservationListComponent implements OnInit{

  protected currentReservations: Reservation[] = [];
  private reservationService = inject(ReservationService);

  ngOnInit(): void {
    this.reservationService.getCurrentReservations().subscribe((data) => {
      this.currentReservations = data;
    });
  }

  trackById(index: number, reservation: Reservation): number {
    return reservation.id;
  }

  cancelReservation(reservationId: number): void {
    this.reservationService.cancelMyReservation(reservationId).subscribe(() => {
      this.currentReservations = this.currentReservations.filter(res => res.id !== reservationId);
    });
  }

}
