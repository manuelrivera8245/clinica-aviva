import { Injectable } from '@angular/core';
import { AuthResponse } from '../models/auth-response.model';
import { UsuarioSesion, RolUsuario } from '../models/usuario.model';

const TOKEN_KEY = 'aviva_auth_token';
const USER_KEY  = 'aviva_user';

// Servicio de gestión de JWT y sesión
@Injectable({
  providedIn: 'root'
})
export class TokenService {

  /** Guarda el token JWT en localStorage */
  saveToken(token: string): void {
    localStorage.setItem(TOKEN_KEY, token);
  }

  /** Obtiene el token JWT desde localStorage (null si no existe) */
  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  /** Guarda los datos del usuario autenticado en localStorage */
  saveUser(response: AuthResponse): void {
    const user: UsuarioSesion = {
      idUsuario: response.idUsuario,
      nombres:   response.nombres,
      apellidos: response.apellidos,
      correo:    response.correo,
      rol:       response.rol
    };
    localStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  /** Obtiene el usuario de sesión desde localStorage (null si no existe) */
  getUser(): UsuarioSesion | null {
    const raw = localStorage.getItem(USER_KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as UsuarioSesion;
    } catch {
      return null;
    }
  }

  /** Elimina token y datos de usuario (logout) */
  clearSession(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  }

  /** Verifica si existe un token en sesión */
  hasToken(): boolean {
    return !!this.getToken();
  }

  /** Decodifica el payload base64 del JWT sin verificar firma */
  decodePayload(): Record<string, unknown> | null {
    const token = this.getToken();
    if (!token) return null;
    try {
      const parts = token.split('.');
      if (parts.length !== 3) return null;
      const payload = parts[1];
      // Añadir padding Base64
      const padded = payload + '='.repeat((4 - payload.length % 4) % 4);
      const decoded = atob(padded);
      return JSON.parse(decoded);
    } catch {
      return null;
    }
  }

  /** Obtiene el rol del usuario desde el payload del JWT */
  getRolFromToken(): RolUsuario | null {
    const payload = this.decodePayload();
    if (!payload) return null;
    // Extraer rol del payload
    return (payload['rol'] as RolUsuario) || null;
  }

  /** Verifica si el token ha expirado (compara claim "exp" con el tiempo actual) */
  isTokenExpired(): boolean {
    const payload = this.decodePayload();
    if (!payload || !payload['exp']) return true;
    const expMs = (payload['exp'] as number) * 1000;
    return Date.now() > expMs;
  }
}
