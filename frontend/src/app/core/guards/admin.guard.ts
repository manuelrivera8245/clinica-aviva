import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Guard de rol ADMINISTRADOR.
 * Solo permite acceso si el usuario autenticado tiene rol 'ADMINISTRADOR'.
 */
@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    if (this.authService.getRolActual() === 'ADMINISTRADOR') {
      return true;
    }
    this.router.navigate(['/inicio']);
    return false;
  }
}
