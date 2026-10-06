package com.clinicaaviva.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO de respuesta para un turno disponible en la agenda.
 * Equivalente a la vista: vista_turnos_disponibles
 *
 * BUG FIX: Se agrego @JsonFormat en fecha, horaInicio y horaFin para garantizar
 * serializacion consistente como strings ISO-8601, independientemente de la
 * configuracion global de Jackson (write-dates-as-timestamps).
 * Sin @JsonFormat, en algunas configuraciones Jackson serializa LocalDate como
 * array [2026,6,4] y LocalTime como [8,0], causando errores en el frontend Angular.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TurnoDisponibleResponse {

    private Integer idTurno;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaFin;

    // Datos del medico
    private Integer idMedico;
    private String nombreMedico;

    // Datos de la especialidad
    private Integer idEspecialidad;
    private String especialidad;
}
