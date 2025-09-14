import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Spaceship} from '../models/spaceship';

@Injectable({
  providedIn: 'root'
})
export class SpaceshipService {
  private apiUrl = 'http://localhost:8080/api/ships';

  constructor(private httpClient: HttpClient) {}

  // GET - alle schepen van gebruiker
  getAllSpaceships(): Observable<Spaceship[]> {
    return this.httpClient.get<Spaceship[]>(this.apiUrl);
  }

  // POST - voeg nieuw schip toe voor gebruiker
  createSpaceship(spaceship: Spaceship): Observable<Spaceship> {
    return this.httpClient.post<Spaceship>(this.apiUrl, spaceship);
  }

  // PUT - update schip van gebruiker
  updateSpaceship(spaceship: Spaceship): Observable<Spaceship> {
    return this.httpClient.put<Spaceship>(`${this.apiUrl}/${spaceship.shipId}`, spaceship);
  }
}
