package com.clinicaaviva.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitar la cancelacion de una cita (RF-05).
 * El paciente solo puede cancelar con al menos 24 horas de anticipacion.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelacionCitaRequest {

    @NotNull(message = "El ID de la cita es obligatorio")
    private Integer idCita;
}
