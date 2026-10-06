package com.clinicaaviva.model.enums;

/**
 * Estados posibles de un turno en la agenda medica.
 * Libre    = Disponible para reserva
 * Ocupado  = Ya fue reservado por un paciente
 * Bloqueado = No disponible (descanso, emergencia, etc.)
 */
public enum EstadoTurno {
    Libre,
    Ocupado,
    Bloqueado
}
