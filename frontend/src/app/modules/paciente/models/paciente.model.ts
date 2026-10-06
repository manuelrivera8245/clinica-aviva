/**
 * Modelo Paciente — coincide con PacienteResponse.java y la tabla paciente.
 */
export interface Paciente {
  idPaciente: number;
  dni: string;
  nombres: string;
  apellidos: string;
  correo: string;
  telefono: string;
  fechaRegistro: string;
  activo: boolean;
}

/**
 * Modelo de respuesta para citas — coincide con CitaResponse.java
 */
export interface CitaResponse {
  idCita: number;
  fechaReserva: string;
  estado: EstadoCita;
  idPaciente: number;
  nombrePaciente: string;
  dniPaciente: string;
  telefonoPaciente: string;
  idTurno: number;
  fechaCita: string;
  horaInicio: string;
  horaFin: string;
  idMedico: number;
  nombreMedico: string;
  idEspecialidad: number;
  nombreEspecialidad: string;
}

export type EstadoCita = 'Programada' | 'Atendida' | 'No Asistio' | 'Cancelada';

/**
 * Modelo de turno disponible — coincide con TurnoDisponibleResponse.java
 */
export interface TurnoDisponible {
  idTurno: number;
  fecha: string;
  horaInicio: string;
  horaFin: string;
  idMedico: number;
  nombreMedico: string;
  idEspecialidad: number;
  especialidad: string;
}
