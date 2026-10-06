package com.clinicaaviva.controller;

import com.clinicaaviva.dto.response.ApiResponse;
import com.clinicaaviva.dto.response.CitaResponse;
import com.clinicaaviva.dto.response.DashboardMedicoResponse;
import com.clinicaaviva.dto.response.DashboardResponse;
import com.clinicaaviva.dto.response.DashboardResponse.CitaPorEstado;
import com.clinicaaviva.model.enums.EstadoCita;
import com.clinicaaviva.repository.CitaRepository;
import com.clinicaaviva.security.UserPrincipal;
import com.clinicaaviva.service.CitaService;
import com.clinicaaviva.service.MedicoService;
import com.clinicaaviva.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Controlador REST para el dashboard administrativo.
 * Proporciona KPIs, metricas y datos agregados del sistema.
 *
 * GET /api/dashboard/resumen — unico endpoint, rol ADMINISTRADOR.
 * Los campos de respuesta estan alineados con DashboardAdminData (admin.service.ts).
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    private final CitaService citaService;
    private final MedicoService medicoService;
    private final PacienteService pacienteService;
    private final CitaRepository citaRepository;

    /**
     * GET /api/dashboard/resumen
     * Devuelve el resumen completo del dashboard administrativo.
     *
     * Campos calculados:
     *   - totalCitas        : COUNT(*) de todas las citas historicas
     *   - totalPacientes    : COUNT(*) de la tabla paciente
     *   - totalMedicos      : COUNT(*) de medicos activos
     *   - citasHoy          : citas cuyo turno es hoy
     *   - tasaAusentismo    : (No_Asistio / totalCitas) * 100; 0.0 si sin citas
     *   - citasPorEstado    : desglose { estado, cantidad } por cada EstadoCita
     *   - citasDelDia       : lista completa de citas de hoy (para tabla del monitor)
     *
     * ROL: ADMINISTRADOR
     */
    @GetMapping("/resumen")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<DashboardResponse>> obtenerResumen() {

        // --- Conteos base ---
        long totalMedicos      = medicoService.contarMedicosActivos();
        long totalPacientes    = pacienteService.contarPacientes();

        // --- Citas del dia (listado completo para la tabla del monitor) ---
        List<CitaResponse> citasDelDia = citaService.listarCitasDelDiaAdmin();
        long citasHoy = citaRepository.countCitasHoy();

        // --- Total historico por estado ---
        long totalProgramadas    = citaRepository.countByEstado(EstadoCita.Programada);
        long totalAtendidas      = citaRepository.countByEstado(EstadoCita.Atendida);
        long totalNoAsistio      = citaRepository.countByEstado(EstadoCita.No_Asistio);
        long totalCanceladas     = citaRepository.countByEstado(EstadoCita.Cancelada);
        long totalCitas          = totalProgramadas + totalAtendidas + totalNoAsistio + totalCanceladas;

        // --- Tasa de ausentismo ---
        double tasaAusentismo = (totalCitas > 0)
                ? ((double) totalNoAsistio / totalCitas) * 100.0
                : 0.0;

        // --- Desglose por estado (alineado con { estado: string, cantidad: number } de TS) ---
        List<CitaPorEstado> citasPorEstado = Arrays.asList(
                CitaPorEstado.builder().estado("Programada").cantidad(totalProgramadas).build(),
                CitaPorEstado.builder().estado("Atendida").cantidad(totalAtendidas).build(),
                CitaPorEstado.builder().estado("No Asistio").cantidad(totalNoAsistio).build(),
                CitaPorEstado.builder().estado("Cancelada").cantidad(totalCanceladas).build()
        );

        DashboardResponse resumen = DashboardResponse.builder()
                .totalCitas(totalCitas)
                .totalPacientes(totalPacientes)
                .totalMedicos(totalMedicos)
                .citasHoy(citasHoy)
                .tasaAusentismo(tasaAusentismo)
                .citasPorEstado(citasPorEstado)
                .citasDelDia(citasDelDia)
                .build();

        return ResponseEntity.ok(ApiResponse.ok(resumen));
    }

    /**
     * GET /api/dashboard/medico
     * Devuelve KPIs del día para el médico autenticado.
     *
     * Soluciona GAP-08: agenda.service.ts.getDashboardMedico() llamaba este endpoint
     * que no existía → 404 latente. Ahora el contrato está cerrado.
     *
     * Campos devueltos:
     *   - citasHoy       : total citas de hoy del médico (todos los estados)
     *   - programadas    : pendientes de atender
     *   - atendidas      : ya marcadas como Atendida
     *   - noAsistio      : marcadas como No Asistio
     *   - tasaAusentismo : (noAsistio / citasHoy) * 100
     *   - citasDelDia    : lista completa para la tabla del panel
     *
     * ROL: MEDICO
     */
    @GetMapping("/medico")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<ApiResponse<DashboardMedicoResponse>> obtenerResumenMedico(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        List<CitaResponse> citasDelDia = citaService.listarAgendaDelDia(userPrincipal.getId());

        long totalHoy     = citasDelDia.size();
        long programadas  = citasDelDia.stream().filter(c -> c.getEstado() == EstadoCita.Programada).count();
        long atendidas    = citasDelDia.stream().filter(c -> c.getEstado() == EstadoCita.Atendida).count();
        long noAsistio    = citasDelDia.stream().filter(c -> c.getEstado() == EstadoCita.No_Asistio).count();
        double tasa       = (totalHoy > 0) ? ((double) noAsistio / totalHoy) * 100.0 : 0.0;


        DashboardMedicoResponse resumen = DashboardMedicoResponse.builder()
                .citasHoy(totalHoy)
                .programadas(programadas)
                .atendidas(atendidas)
                .noAsistio(noAsistio)
                .tasaAusentismo(tasa)
                .citasDelDia(citasDelDia)
                .build();

        return ResponseEntity.ok(ApiResponse.ok(resumen));
    }
}

