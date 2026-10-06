/**
 * Modelo Médico — coincide con MedicoResponse.java
 */
export interface MedicoResponse {
  idMedico: number;
  nombres: string;
  apellidos: string;
  correo: string;
  usuario: string;
  activo: boolean;
  idEspecialidad: number;
  nombreEspecialidad: string;
  dni?: string;
  telefono?: string;
  fechaRegistro?: string | Date;
}
