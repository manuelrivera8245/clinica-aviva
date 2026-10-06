import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Guard de rol PACIENTE.
 * Solo permite acceso si el usuario autenticado tiene rol 'PACIENTE'.
 */
@Injectable({
  providedIn: 'root'
})
export class PacienteGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    if (this.authService.getRolActual() === 'PACIENTE') {
      return true;
    }
    this.router.navigate(['/inicio']);
    return false;
  }
}
