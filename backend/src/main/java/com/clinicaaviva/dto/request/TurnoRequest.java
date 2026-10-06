package com.clinicaaviva.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO de solicitud para crear un turno en la agenda de un medico.
 * Alineado con la interfaz TurnoRequest de admin.service.ts:
 *   { idMedico: number; fecha: string; horaInicio: string; horaFin: string }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TurnoRequest {

    /** ID del medico al que pertenece el turno. */
    private Integer idMedico;

    /** Fecha del turno (formato ISO: yyyy-MM-dd). */
    private LocalDate fecha;

    /** Hora de inicio del turno (formato ISO: HH:mm). */
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    /** Hora de fin del turno (formato ISO: HH:mm). */
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaFin;
}
