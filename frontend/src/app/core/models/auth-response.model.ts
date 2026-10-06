import { RolUsuario } from './usuario.model';

/**
 * Respuesta del backend tras un login o registro exitoso.
 * Coincide con JwtAuthenticationResponse.java
 */
export interface AuthResponse {
  token: string;
  tipo: string;          // "Bearer"
  idUsuario: number;
  nombres: string;
  apellidos: string;
  correo: string;
  rol: RolUsuario;
}
