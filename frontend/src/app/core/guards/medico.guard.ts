import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Guard de rol MEDICO.
 * Solo permite acceso si el usuario autenticado tiene rol 'MEDICO'.
 */
@Injectable({
  providedIn: 'root'
})
export class MedicoGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    if (this.authService.getRolActual() === 'MEDICO') {
      return true;
    }
    this.router.navigate(['/inicio']);
    return false;
  }
}
