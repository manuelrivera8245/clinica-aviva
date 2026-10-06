/**
 * Wrapper genérico de respuesta del backend.
 * Coincide con ApiResponse.java
 *
 * Forma:
 * {
 *   exito: boolean,
 *   mensaje: string,
 *   datos: T,
 *   timestamp: string
 * }
 */
export interface ApiResponse<T> {
  exito: boolean;
  mensaje: string;
  datos: T;
  timestamp: string;
}