import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from '../../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import { AuthResponse } from '../models/auth-response.model';
import { UsuarioSesion, RolUsuario } from '../models/usuario.model';
import { TokenService } from './token.service';

export interface LoginRequest {
  credencial: string;   // DNI, correo o usuario
  contrasena: string;
}

export interface RegistroPacienteRequest {
  dni: string;
  nombres: string;
  apellidos: string;
  correo: string;
  telefono?: string;
  contrasena: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly apiUrl = `${environment.apiUrl}/auth`;

  constructor(
    private http: HttpClient,
    private tokenService: TokenService,
    private router: Router
  ) { }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<ApiResponse<AuthResponse>>(
      `${this.apiUrl}/login`, request
    ).pipe(
      map(response => response.datos),
      tap(authData => {
        this.tokenService.saveToken(authData.token);
        this.tokenService.saveUser(authData);
      })
    );
  }

  registrar(request: RegistroPacienteRequest): Observable<AuthResponse> {
    return this.http.post<ApiResponse<AuthResponse>>(
      `${this.apiUrl}/registro`, request
    ).pipe(
      map(response => response.datos),
      tap(authData => {
        this.tokenService.saveToken(authData.token);
        this.tokenService.saveUser(authData);
      })
    );
  }

  verificarCredencial(credencial: string): Observable<boolean> {
    return this.http.get<ApiResponse<boolean>>(
      `${this.apiUrl}/verificar`, { params: { credencial } }
    ).pipe(map(r => r.datos));
  }

  logout(): void {
    this.tokenService.clearSession();
    this.router.navigate(['/auth/login']);
  }

  isAuthenticated(): boolean {
    return this.tokenService.hasToken() && !this.tokenService.isTokenExpired();
  }

  getRolActual(): RolUsuario | null {
    return this.tokenService.getUser()?.rol ?? null;
  }

  getUsuarioActual(): UsuarioSesion | null {
    return this.tokenService.getUser();
  }

  redirigirSegunRol(): void {
    const rol = this.getRolActual();
    switch (rol) {
      case 'PACIENTE': this.router.navigate(['/paciente/dashboard']); break;
      case 'MEDICO': this.router.navigate(['/medico/dashboard']); break;
      case 'ADMINISTRADOR': this.router.navigate(['/admin/dashboard']); break;
      default: this.router.navigate(['/inicio']); break;
    }
  }
}
