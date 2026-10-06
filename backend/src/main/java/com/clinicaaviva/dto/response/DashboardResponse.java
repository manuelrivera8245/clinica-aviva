package com.clinicaaviva.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de respuesta para el dashboard administrativo.
 *
 * Campos alineados exactamente con la interfaz DashboardAdminData del frontend
 * (admin.service.ts) para evitar el desajuste de contrato:
 *   - totalCitas        (antes: no existia)
 *   - totalPacientes    (antes: totalPacientesRegistrados)
 *   - totalMedicos      (antes: totalMedicosActivos)
 *   - citasHoy          (antes: totalCitasHoy)
 *   - tasaAusentismo    (antes: no existia)
 *   - citasPorEstado    (antes: no existia)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    /** Total historico de todas las citas registradas en el sistema. */
    private long totalCitas;

    /** Total de pacientes registrados. */
    private long totalPacientes;

    /** Total de medicos activos. */
    private long totalMedicos;

    /** Cantidad de citas programadas para hoy. */
    private long citasHoy;

    /**
     * Tasa de ausentismo en porcentaje (0-100).
     * Calculado como: (No_Asistio / totalCitas) * 100
     * Devuelve 0.0 si no hay citas.
     */
    private double tasaAusentismo;

    /** Desglose de citas agrupadas por estado (para tabla del dashboard). */
    private List<CitaPorEstado> citasPorEstado;

    /** Listado de citas del dia actual (para tabla de monitor). */
    private List<CitaResponse> citasDelDia;

    /**
     * Sub-DTO para el desglose por estado.
     * Coincide con { estado: string; cantidad: number } de TypeScript.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CitaPorEstado {
        private String estado;
        private long cantidad;
    }
}
