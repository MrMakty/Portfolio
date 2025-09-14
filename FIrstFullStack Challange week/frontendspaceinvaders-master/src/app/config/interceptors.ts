import { HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { LoginService } from '../services/login.service';
import { Router } from '@angular/router';

export function authInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  const loginService = inject(LoginService);
  const router = inject(Router);

  const authToken = loginService.getToken();

  console.log('[AuthInterceptor] Intercepting request:', req.url);

  if (authToken) {
    console.log('[AuthInterceptor] Token gevonden.');

    if (!loginService.isTokenExpired()) {
      console.log('[AuthInterceptor] Token is geldig. Authorization-header wordt toegevoegd.');
      const newRequest = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${authToken}`),
      });
      return next(newRequest);
    } else {
      console.warn('[AuthInterceptor] Token is verlopen. Gebruiker wordt uitgelogd.');
      loginService.logout();
      router.navigate(['/auth']);
    }

  } else {
    console.log('[AuthInterceptor] Geen token aanwezig. Request gaat zonder Authorization-header.');
  }

  return next(req); // Zonder Authorization-header
}
