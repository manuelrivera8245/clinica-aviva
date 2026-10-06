package com.clinicaaviva.controller;

import com.clinicaaviva.dto.request.GenerarTurnosRequest;
import com.clinicaaviva.dto.request.HorarioMedicoRequest;
import com.clinicaaviva.dto.response.ApiResponse;
import com.clinicaaviva.dto.response.HorarioMedicoResponse;
import com.clinicaaviva.security.UserPrincipal;
import com.clinicaaviva.service.HorarioMedicoService;
import com.clinicaaviva.service.TurnoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de horarios de atención médica (RF-11).
 *
 * Endpoints:
 *   GET    /api/horarios?medicoId={id}   → lista horarios activos de un médico
 *   POST   /api/horarios                 → crea un nuevo horario
 *   DELETE /api/horarios/{id}            → desactiva un horario (borrado lógico)
 *   POST   /api/horarios/generar-turnos  → genera turnos automáticos en un rango de fechas
 *
 * ROL: ADMINISTRADOR
 */
@RestController
@RequestMapping("/api/horarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class HorarioMedicoController {

    private final HorarioMedicoService horarioService;
    private final TurnoService         turnoService;

    /**
     * GET /api/horarios?medicoId={id}
     * Lista los horarios activos del médico indicado.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<List<HorarioMedicoResponse>>> listar(
            @RequestParam Integer medicoId) {
        return ResponseEntity.ok(ApiResponse.ok(horarioService.listarPorMedico(medicoId)));
    }

    /**
     * POST /api/horarios
     * Crea un nuevo bloque horario para el médico.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<HorarioMedicoResponse>> crear(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody HorarioMedicoRequest request) {
        HorarioMedicoResponse resultado = horarioService.crear(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.ok("Horario creado exitosamente", resultado));
    }

    /**
     * DELETE /api/horarios/{id}
     * Desactiva un horario (borrado lógico, no afecta turnos ya generados).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<String>> eliminar(@PathVariable Integer id) {
        horarioService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Horario desactivado"));
    }

    /**
     * POST /api/horarios/generar-turnos
     * Genera automáticamente los turnos para el médico en el rango de fechas indicado,
     * basándose en sus horarios activos configurados.
     */
    @PostMapping("/generar-turnos")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<String>> generarTurnos(
            @Valid @RequestBody GenerarTurnosRequest request) {
        turnoService.generarTurnosDesdeHorario(
                request.getIdMedico(),
                request.getFechaDesde(),
                request.getFechaHasta());
        return ResponseEntity.ok(ApiResponse.ok(
                "Turnos generados correctamente para el período " +
                request.getFechaDesde() + " → " + request.getFechaHasta()));
    }
}
