import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Dock} from '../models/dock';
import {Reservation} from '../models/reservation';
import {DockStatusDTO} from "../dto/DockStatusDTO";

@Injectable({
  providedIn: 'root'
})
export class DockService {

  private baseUrl = `http://localhost:8080/api/docks`;

  constructor(private http: HttpClient) {}

  getAllDocks(): Observable<Dock[]> {
    return this.http.get<Dock[]>(this.baseUrl);
  }

  getReservationsForDock(id: number): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${this.baseUrl}/${id}/reservations`);
  }

  updateMaintenanceStatus(id: number, inMaintenance: boolean): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}/maintenance`, { inMaintenance }, { responseType: 'text' as 'json' });
  }

  getDockStatus(id: number): Observable<DockStatusDTO> {
    return this.http.get<DockStatusDTO>(`${this.baseUrl}/${id}/status`);
  }
}
