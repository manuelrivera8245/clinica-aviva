package com.clinicaaviva.model.enums;

/**
 * Roles de usuario del sistema utilizados por Spring Security.
 * PACIENTE      = Usuario que reserva y gestiona sus citas
 * MEDICO        = Profesional que atiende pacientes
 * ADMINISTRADOR = Gestiona medicos, horarios y configuracion del sistema
 */
public enum RolUsuario {
    PACIENTE,
    MEDICO,
    ADMINISTRADOR
}
