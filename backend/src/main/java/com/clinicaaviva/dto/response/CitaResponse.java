package com.clinicaaviva.dto.response;

import com.clinicaaviva.model.enums.EstadoCita;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO de respuesta para una cita medica.
 * Incluye datos del paciente, medico, turno y especialidad.
 *
 * BUG FIX: Se agrego @JsonFormat en todos los campos java.time para garantizar
 * serializacion como strings ISO-8601 legibles por Angular, independientemente
 * de la configuracion global de Jackson.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaResponse {

    private Integer idCita;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaReserva;

    private EstadoCita estado;

    // Datos del paciente
    private Integer idPaciente;
    private String nombrePaciente;
    private String dniPaciente;
    private String telefonoPaciente;

    // Datos del turno y medico
    private Integer idTurno;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaCita;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaFin;

    // Datos del medico
    private Integer idMedico;
    private String nombreMedico;

    // Datos de la especialidad
    private Integer idEspecialidad;
    private String nombreEspecialidad;
}
