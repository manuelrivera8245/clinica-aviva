/**
 * Tipos para el sistema de roles de Clínica Aviva.
 * Deben coincidir exactamente con el enum RolUsuario.java del backend.
 */
export type RolUsuario = 'PACIENTE' | 'MEDICO' | 'ADMINISTRADOR';

/**
 * Modelo genérico del usuario autenticado almacenado en localStorage.
 */
export interface UsuarioSesion {
  idUsuario: number;
  nombres: string;
  apellidos: string;
  correo: string;
  rol: RolUsuario;
}
