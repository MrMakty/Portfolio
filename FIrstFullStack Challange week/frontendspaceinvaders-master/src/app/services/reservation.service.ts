import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {forkJoin, Observable, switchMap} from 'rxjs';
import {Reservation} from '../models/reservation';
import {CreateReservationDTO} from '../dto/CreateReservationDTO';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private apiUrl = 'http://localhost:8080/api/reservations';

  constructor(private httpClient: HttpClient) {}

  createReservation(reservation: CreateReservationDTO): Observable<Reservation> {
    const body = {
      dockId: reservation.dockId,
      spaceshipId: reservation.spaceshipId,
      startTimestamp: reservation.startTimestamp,
      endTimestamp: reservation.endTimestamp
    };
    console.log(body);
    return this.httpClient.post<Reservation>(this.apiUrl, body);
  }

  getAllReservations(): Observable<Reservation[]> {
    return this.httpClient.get<Reservation[]>(this.apiUrl);
  }

  getActiveReservations(): Observable<Reservation[]> {
    return this.httpClient.get<Reservation[]>(`${this.apiUrl}/active`);
  }

  getReservationById(id: number): Observable<Reservation> {
    return this.httpClient.get<Reservation>(`${this.apiUrl}/${id}`);
  }

  extendReservation(id: number, newStartTimestamp: Date): Observable<Reservation> {
    return this.httpClient.put<Reservation>(`${this.apiUrl}/${id}`, {
      newStartTimestamp
    });
  }

  cancelReservation(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.apiUrl}/${id}`);
  }

  cancelAllReservationsForDock(dockId: number): Observable<void[]> {
    return this.getAllReservations().pipe(
      // Filter alleen de reservations van deze dock
      switchMap((reservations) => {
        const dockReservations = reservations.filter(r => r.dock.id === dockId);
        const cancelRequests = dockReservations.map(res => this.cancelReservation(res.id));
        return forkJoin(cancelRequests);
      })
    );
  }

  getCurrentReservations(): Observable<Reservation[]> {
    return this.httpClient.get<Reservation[]>(`${this.apiUrl}`);
  }

  cancelMyReservation(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.apiUrl}/myreservation/${id}`);
  }

}
