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

/**
 * Servicio de autenticación.
 * Gestiona login, registro, logout y estado de autenticación.
 * POST /api/auth/login  →  AuthResponse
 * POST /api/auth/registro → AuthResponse
 */
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

  /**
   * POST /api/auth/login
   * Autentica al usuario y guarda token + datos de sesión.
   */
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

  /**
   * POST /api/auth/registro
   * Registra un nuevo paciente y autentica inmediatamente.
   */
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

  /**
   * GET /api/auth/verificar?credencial={valor}
   * Verifica si un DNI, correo o usuario ya está registrado.
   */
  verificarCredencial(credencial: string): Observable<boolean> {
    return this.http.get<ApiResponse<boolean>>(
      `${this.apiUrl}/verificar`, { params: { credencial } }
    ).pipe(map(r => r.datos));
  }

  /** Cierra sesión: limpia localStorage y redirige a login */
  logout(): void {
    this.tokenService.clearSession();
    this.router.navigate(['/auth/login']);
  }

  /** ¿Hay un token válido y no expirado en sesión? */
  isAuthenticated(): boolean {
    return this.tokenService.hasToken() && !this.tokenService.isTokenExpired();
  }

  /** Rol del usuario actual (desde localStorage) */
  getRolActual(): RolUsuario | null {
    return this.tokenService.getUser()?.rol ?? null;
  }

  /** Datos completos del usuario de sesión */
  getUsuarioActual(): UsuarioSesion | null {
    return this.tokenService.getUser();
  }

  /** Redirige al dashboard correcto según el rol */
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
