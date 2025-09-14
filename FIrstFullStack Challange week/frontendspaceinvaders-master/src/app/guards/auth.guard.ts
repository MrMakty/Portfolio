import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { LoginService } from '../services/login.service';

export const authGuard: CanActivateFn = () => {
  console.log("Ik ben in authguard")
  const loginService = inject(LoginService);
  const router = inject(Router);

  if (loginService.isLoggedIn()) {
    console.log('[AuthGuard] Access granted.');
    return true;
  }

  console.warn('[AuthGuard] No valid session. Redirecting to /login');
  loginService.logout(); // Zorgt dat token wordt verwijderd etc.
  router.navigate(['login']); // ✅ Mag hier
  return false;
};
