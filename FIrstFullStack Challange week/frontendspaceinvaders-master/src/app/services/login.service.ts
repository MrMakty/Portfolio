import { inject, Injectable } from '@angular/core';
import { Login } from '../models/login';
import { Register } from '../models/register';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Token } from '../models/token';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private httpClient = inject(HttpClient);
  private router = inject(Router);
  private loggedIn: boolean = false;
  private token: string | null = null;

  constructor() {
    console.log('[LoginService] Initialisatie gestart.');
    this.loadTokenFromLocalStorage();
    if (this.token) {
      console.log('[LoginService] Token gevonden bij opstart.');
      if (!this.isTokenExpired()) {
        this.loggedIn = true;
        console.log('[LoginService] Bestaande sessie is geldig. Gebruiker is ingelogd.');
      } else {
        console.warn('[LoginService] Bestaande token is verlopen. Gebruiker wordt uitgelogd.');
        this.logout();
      }
    } else {
      console.log('[LoginService] Geen token gevonden bij opstart.');
    }
  }

  public isLoggedIn(): boolean {
    const token = this.getToken();
    const valid = token !== null && !this.isTokenExpired();
    console.log(`[LoginService] isLoggedIn(): ${valid}`);
    return valid;
  }

  public getToken(): string | null {
    this.loadTokenFromLocalStorage();
    console.log(`[LoginService] getToken(): ${this.token ? 'Token aanwezig' : 'Geen token gevonden'}`);
    return this.token;
  }

  public login(login: Login): Observable<Token> {
    console.log('[LoginService] Login poging gestart...');
    return this.httpClient.post<Token>(
      'http://localhost:8080/api/auth/login',
      login
    ).pipe(
      tap(token => {
        if (token.token) {
          this.loggedIn = true;
          this.token = token.token;
          this.saveTokenInLocalStorage(token.token);
          console.log('[LoginService] Login succesvol. Token opgeslagen.');
          this.router.navigate(['']);
        } else {
          console.warn('[LoginService] Login response bevat geen token.');
        }
      })
    );
  }

  public registerAccount(register: Register): Observable<Token> {
    return this.httpClient.post<Token>(
      'http://localhost:8080/api/auth/register',
      {
        email: register.email,
        password: register.password,
        firstName: register.firstName,
        lastName: register.lastName,
        occupation: register.occupation
      }
    ).pipe(
      tap(token => {
        if (token.token) {
          this.loggedIn = true;
          this.token = token.token;
          this.saveTokenInLocalStorage(token.token);
          console.log('[LoginService] Registratie succesvol. Token opgeslagen.');
        } else {
          console.warn('[LoginService] Registratie response bevat geen token.');
        }
      })
    );
  }

  public logout(): void {
    console.log('[LoginService] Uitloggen gestart.');
    this.loggedIn = false;
    this.token = null;
    localStorage.removeItem('authToken');
    this.router.navigate(['login']);
    console.log('[LoginService] Sessiedata verwijderd. Navigatie naar /.');
  }

  public isTokenExpired(): boolean {
    if (!this.token) {
      console.warn('[LoginService] isTokenExpired(): Geen token aanwezig.');
      return true;
    }

    const payload = this.parseJwt(this.token);
    if (!payload || !payload.exp) {
      console.warn('[LoginService] isTokenExpired(): Ongeldig payloadformaat.');
      return true;
    }

    const expiry = payload.exp * 1000;
    const isExpired = Date.now() > expiry;
    console.log(`[LoginService] isTokenExpired(): ${isExpired ? 'Token is verlopen.' : 'Token is geldig.'}`);
    return isExpired;
  }

  private parseJwt(token: string): any | null {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64).split('').map((c) =>
          '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
        ).join('')
      );
      return JSON.parse(jsonPayload);
    } catch (e) {
      console.error('[LoginService] parseJwt(): Fout bij het parsen van JWT.', e);
      return null;
    }
  }

  private saveTokenInLocalStorage(token: string): void {
    localStorage.setItem('authToken', token);
    console.log('[LoginService] Token opgeslagen in localStorage.');
  }

  private loadTokenFromLocalStorage(): void {
    this.token = localStorage.getItem('authToken');
    console.log(`[LoginService] Token geladen uit localStorage: ${this.token ? 'Aanwezig' : 'Ontbreekt'}`);
  }

  public resetToken(): void {
    this.token = null;
  }
}
