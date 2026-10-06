package com.clinicaaviva.dto.request;

import com.clinicaaviva.model.enums.EstadoCita;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para que el medico actualice el estado de una cita a "Atendida" o "No Asistio" (RF-09).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarEstadoCitaRequest {

    @NotNull(message = "El ID de la cita es obligatorio")
    private Integer idCita;

    @NotNull(message = "El nuevo estado es obligatorio")
    private EstadoCita estado;
}
