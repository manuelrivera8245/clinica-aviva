package com.clinicaaviva.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

/**
 * DTO para solicitar la generación automática de turnos a partir de los
 * horarios configurados de un médico (RF-11).
 * Usado en POST /api/horarios/generar-turnos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerarTurnosRequest {

    @NotNull(message = "El médico es requerido")
    private Integer idMedico;

    @NotNull(message = "La fecha de inicio es requerida")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaDesde;

    @NotNull(message = "La fecha de fin es requerida")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaHasta;
}
