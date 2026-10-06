package com.clinicaaviva.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitar la reserva de una cita (RF-04).
 * Contiene el ID del paciente autenticado y el ID del turno seleccionado.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaCitaRequest {

    @NotNull(message = "El ID del turno es obligatorio")
    private Integer idTurno;
}
