package com.clinicaaviva.dto.response;

import lombok.*;
import java.time.LocalTime;

/**
 * DTO de respuesta para un horario de atención de un médico.
 * Expuesto por GET /api/horarios?medicoId={id}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HorarioMedicoResponse {

    private Integer idHorario;
    private Integer idMedico;
    private String  nombreMedico;
    private Integer diaSemana;      // 1=Lun … 7=Dom
    private String  diaNombre;      // "Lunes", "Martes", etc.
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer duracionTurnoMin;
    private Boolean activo;
}
