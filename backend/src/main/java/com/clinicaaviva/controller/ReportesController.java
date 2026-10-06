package com.clinicaaviva.controller;

import com.clinicaaviva.dto.response.ApiResponse;
import com.clinicaaviva.dto.response.AusentismoMedicoResponse;
import com.clinicaaviva.dto.response.CitaResponse;
import com.clinicaaviva.entity.Cita;
import com.clinicaaviva.model.enums.EstadoCita;
import com.clinicaaviva.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para reportes administrativos.
 *
 * Endpoints:
 *   GET /api/reportes/citas?desde=&hasta=          → Lista de citas en rango (CitaResponse[])
 *   GET /api/reportes/ausentismo                   → Citas con No Asistio en el ultimo mes
 *   GET /api/reportes/ausentismo-por-medico        → Estadísticas agrupadas por médico (GAP-13)
 *
 * ROL: ADMINISTRADOR
 */
@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ReportesController {

    private final CitaRepository citaRepository;

    /**
     * GET /api/reportes/citas?desde=yyyy-MM-dd&hasta=yyyy-MM-dd
     * Lista citas en un rango de fechas para exportacion/revision.
     * Si no se especifican fechas, usa el mes actual.
     * ROL: ADMINISTRADOR
     */
    @GetMapping("/citas")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<List<CitaResponse>>> reporteCitas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        LocalDate fechaDesde = (desde != null) ? desde : LocalDate.now().withDayOfMonth(1);
        LocalDate fechaHasta = (hasta != null) ? hasta : LocalDate.now();

        List<CitaResponse> citas = citaRepository.findCitasByRangoFecha(fechaDesde, fechaHasta)
                .stream()
                .map(this::mapCitaToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.ok(citas));
    }

    /**
     * GET /api/reportes/ausentismo
     * Devuelve citas individuales con estado No_Asistio del ultimo mes.
     * Mantenido por compatibilidad — el frontend lo usa internamente.
     * ROL: ADMINISTRADOR
     */
    @GetMapping("/ausentismo")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<List<CitaResponse>>> reporteAusentismo() {
        LocalDate desde = LocalDate.now().minusMonths(1);
        LocalDate hasta = LocalDate.now();

        List<CitaResponse> inasistencias = citaRepository
                .findCitasByRangoFecha(desde, hasta)
                .stream()
                .filter(c -> c.getEstado() == EstadoCita.No_Asistio)
                .map(this::mapCitaToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.ok(inasistencias));
    }

    /**
     * GET /api/reportes/ausentismo-por-medico?desde=yyyy-MM-dd&hasta=yyyy-MM-dd
     * Devuelve estadísticas de ausentismo agrupadas por médico.
     *
     * Soluciona GAP-13: el template reportes.component.html accedía a
     * { nombreMedico, especialidad, totalCitas, noAsistio, tasa } pero el endpoint
     * existente devolvía CitaResponse[] individuales → campos undefined en la UI.
     *
     * No requiere nueva query SQL: reutiliza findCitasByRangoFecha y agrupa en Java.
     * Si no se especifica rango, usa el mes actual (igual que /citas).
     *
     * ROL: ADMINISTRADOR
     */
    @GetMapping("/ausentismo-por-medico")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<List<AusentismoMedicoResponse>>> reporteAusentismoPorMedico(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        LocalDate fechaDesde = (desde != null) ? desde : LocalDate.now().withDayOfMonth(1);
        LocalDate fechaHasta = (hasta != null) ? hasta : LocalDate.now();

        // Obtener todas las citas del período (JOIN FETCH incluye médico y especialidad)
        List<Cita> citas = citaRepository.findCitasByRangoFecha(fechaDesde, fechaHasta);

        // Agrupar por médico (clave: idMedico)
        Map<Integer, List<Cita>> porMedico = citas.stream()
                .filter(c -> c.getTurno() != null && c.getTurno().getMedico() != null)
                .collect(Collectors.groupingBy(c -> c.getTurno().getMedico().getIdMedico()));

        List<AusentismoMedicoResponse> resultado = new ArrayList<>();

        for (Map.Entry<Integer, List<Cita>> entry : porMedico.entrySet()) {
            List<Cita> citasMedico = entry.getValue();

            // Datos del médico desde la primera cita del grupo
            var medico = citasMedico.get(0).getTurno().getMedico();
            String nombreMedico = medico.getNombres() + " " + medico.getApellidos();
            String especialidad = (medico.getEspecialidad() != null)
                    ? medico.getEspecialidad().getNombre() : "—";

            long totalCitas  = citasMedico.size();
            long noAsistio   = citasMedico.stream()
                    .filter(c -> c.getEstado() == EstadoCita.No_Asistio)
                    .count();
            double tasa = (totalCitas > 0) ? ((double) noAsistio / totalCitas) * 100.0 : 0.0;

            resultado.add(AusentismoMedicoResponse.builder()
                    .nombreMedico(nombreMedico)
                    .especialidad(especialidad)
                    .totalCitas(totalCitas)
                    .noAsistio(noAsistio)
                    .tasa(tasa)
                    .build());
        }

        // Ordenar por tasa descendente (los médicos con más ausentismo primero)
        resultado.sort((a, b) -> Double.compare(b.getTasa(), a.getTasa()));

        return ResponseEntity.ok(ApiResponse.ok(resultado));
    }

    /**
     * Mapeo de Cita a CitaResponse para los reportes.
     * Incluye idEspecialidad y nombreEspecialidad (corregidos en este fix).
     */
    private CitaResponse mapCitaToResponse(Cita cita) {
        String nombrePaciente = null;
        String dniPaciente    = null;
        Integer idPaciente    = null;

        if (cita.getPaciente() != null) {
            idPaciente    = cita.getPaciente().getIdPaciente();
            nombrePaciente = cita.getPaciente().getNombres() + " " + cita.getPaciente().getApellidos();
            dniPaciente   = cita.getPaciente().getDni();
        }

        String  nombreMedico    = null;
        Integer idMedico        = null;
        String  nombreEspec     = null;
        Integer idEspec         = null;
        LocalDate  fechaCita    = null;
        LocalTime  horaInicio   = null;
        LocalTime  horaFin      = null;
        Integer idTurno         = null;

        if (cita.getTurno() != null) {
            idTurno    = cita.getTurno().getIdTurno();
            fechaCita  = cita.getTurno().getFecha();
            horaInicio = cita.getTurno().getHoraInicio();
            horaFin    = cita.getTurno().getHoraFin();

            if (cita.getTurno().getMedico() != null) {
                idMedico    = cita.getTurno().getMedico().getIdMedico();
                nombreMedico = cita.getTurno().getMedico().getNombres() + " "
                        + cita.getTurno().getMedico().getApellidos();

                if (cita.getTurno().getMedico().getEspecialidad() != null) {
                    idEspec     = cita.getTurno().getMedico().getEspecialidad().getIdEspecialidad();
                    nombreEspec = cita.getTurno().getMedico().getEspecialidad().getNombre();
                }
            }
        }

        return CitaResponse.builder()
                .idCita(cita.getIdCita())
                .estado(cita.getEstado())
                .idPaciente(idPaciente)
                .nombrePaciente(nombrePaciente)
                .dniPaciente(dniPaciente)
                .idTurno(idTurno)
                .fechaCita(fechaCita)
                .horaInicio(horaInicio)
                .horaFin(horaFin)
                .idMedico(idMedico)
                .nombreMedico(nombreMedico)
                .idEspecialidad(idEspec)
                .nombreEspecialidad(nombreEspec)
                .build();
    }
}


