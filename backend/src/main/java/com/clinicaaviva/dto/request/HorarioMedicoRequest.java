package com.clinicaaviva.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * DTO para configurar el horario de atencion de un medico (RF-11).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HorarioMedicoRequest {

    private Integer idHorario;

    @NotNull(message = "El medico es obligatorio")
    private Integer idMedico;

    @NotNull(message = "El dia de la semana es obligatorio")
    @Min(value = 1, message = "El dia debe ser entre 1 (Lunes) y 7 (Domingo)")
    @Max(value = 7, message = "El dia debe ser entre 1 (Lunes) y 7 (Domingo)")
    private Integer diaSemana;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    @NotNull(message = "La duracion del turno es obligatoria")
    @Min(value = 15, message = "La duracion minima es 15 minutos")
    @Max(value = 120, message = "La duracion maxima es 120 minutos")
    @Builder.Default
    private Integer duracionTurnoMin = 30;
}
