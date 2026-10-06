package com.clinicaaviva.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para el reporte de ausentismo agrupado por médico.
 *
 * Soluciona GAP-13: el template reportes.component.html usaba campos
 * { nombreMedico, especialidad, totalCitas, noAsistio, tasa } pero el endpoint
 * GET /api/reportes/ausentismo devolvía CitaResponse[] individuales —
 * los campos de agrupación aparecían undefined en la UI.
 *
 * Este DTO es calculado en ReportesController agrupando las citas del período
 * seleccionado por médico, sin nueva query SQL (reutiliza findCitasByRangoFecha).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AusentismoMedicoResponse {

    /** Nombre completo del médico (nombres + apellidos). */
    private String nombreMedico;

    /** Nombre de la especialidad del médico. */
    private String especialidad;

    /** Total de citas del período para este médico (excluye Programadas abiertas). */
    private long totalCitas;

    /** Cantidad de citas con estado "No Asistio" en el período. */
    private long noAsistio;

    /**
     * Tasa de ausentismo en porcentaje (0–100).
     * Calculado como: (noAsistio / totalCitas) * 100.
     * 0.0 si totalCitas == 0.
     */
    private double tasa;
}
