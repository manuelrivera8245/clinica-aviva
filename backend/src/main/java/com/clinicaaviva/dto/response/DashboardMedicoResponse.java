package com.clinicaaviva.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de respuesta para el dashboard del médico.
 *
 * Soluciona GAP-08: agenda.service.ts tenía getDashboardMedico() que llamaba
 * GET /api/dashboard/medico, pero el endpoint no existía en DashboardController.
 * El componente dashboard del médico evitaba el 404 usando getAgendaDelDia()
 * directamente, pero el método del servicio quedaba apuntando a un 404 latente.
 *
 * Campos devueltos (KPIs del día + resumen del historial):
 *   - citasHoy        : total de citas del médico para hoy (todos los estados)
 *   - programadas     : citas del día aún en estado Programada
 *   - atendidas       : citas del día marcadas como Atendida
 *   - noAsistio       : citas del día marcadas como No Asistio
 *   - tasaAusentismo  : (noAsistio / citasHoy) * 100; 0.0 si sin citas
 *   - citasDelDia     : lista completa de citas de hoy para la tabla del panel
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardMedicoResponse {

    /** Total de citas del médico para hoy (todos los estados). */
    private long citasHoy;

    /** Citas del día aún en estado Programada. */
    private long programadas;

    /** Citas del día marcadas como Atendida. */
    private long atendidas;

    /** Citas del día marcadas como No Asistio. */
    private long noAsistio;

    /**
     * Tasa de ausentismo del día en porcentaje (0–100).
     * Calculado como: (noAsistio / citasHoy) * 100. 0.0 si citasHoy == 0.
     */
    private double tasaAusentismo;

    /** Lista completa de citas del día (para la tabla del panel médico). */
    private List<CitaResponse> citasDelDia;
}
