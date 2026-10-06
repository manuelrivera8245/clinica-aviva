package com.clinicaaviva.controller;

import com.clinicaaviva.dto.response.ApiResponse;
import com.clinicaaviva.dto.response.TurnoDisponibleResponse;
import com.clinicaaviva.service.TurnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para consulta de turnos disponibles.
 * Endpoints publicos utilizados en el flujo de reserva de citas (RF-03).
 */
@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TurnoController {

    private final TurnoService turnoService;

    /**
     * GET /api/turnos/disponibles
     * GET /api/turnos/disponibles?medicoId={id}
     * Lista todos los turnos libres a partir de hoy.
     * Si se proporciona medicoId como query param, filtra por ese medico.
     * BUG FIX: el frontend enviaba ?medicoId=X como query param pero el endpoint
     * lo ignoraba (devolvía todos los turnos sin filtrar).
     * PUBLICO
     */
    @GetMapping("/disponibles")
    public ResponseEntity<ApiResponse<List<TurnoDisponibleResponse>>> listarDisponibles(
            @RequestParam(required = false) Integer medicoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<TurnoDisponibleResponse> turnos;
        if (medicoId != null) {
            turnos = turnoService.listarTurnosDisponiblesPorMedico(medicoId, fecha);
        } else {
            turnos = turnoService.listarTurnosDisponibles();
        }
        return ResponseEntity.ok(ApiResponse.ok(turnos));
    }

    /**
     * GET /api/turnos/disponibles/especialidad/{idEspecialidad}
     * Lista turnos libres filtrados por especialidad.
     * PUBLICO
     */
    @GetMapping("/disponibles/especialidad/{idEspecialidad}")
    public ResponseEntity<ApiResponse<List<TurnoDisponibleResponse>>> listarPorEspecialidad(
            @PathVariable Integer idEspecialidad) {
        List<TurnoDisponibleResponse> turnos = turnoService.listarTurnosDisponiblesPorEspecialidad(idEspecialidad);
        return ResponseEntity.ok(ApiResponse.ok(turnos));
    }

    /**
     * GET /api/turnos/disponibles/medico/{idMedico}
     * Lista turnos libres de un medico especifico.
     * PUBLICO
     */
    @GetMapping("/disponibles/medico/{idMedico}")
    public ResponseEntity<ApiResponse<List<TurnoDisponibleResponse>>> listarPorMedico(
            @PathVariable Integer idMedico,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<TurnoDisponibleResponse> turnos = turnoService.listarTurnosDisponiblesPorMedico(idMedico, fecha);
        return ResponseEntity.ok(ApiResponse.ok(turnos));
    }

    /**
     * GET /api/turnos
     * Lista todos los turnos para el panel administrativo.
     * ROL: ADMINISTRADOR
     *
     * Razon: admin.service.ts llama GET /api/turnos pero el controller
     * solo tenia endpoints bajo /disponibles/**
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<List<TurnoDisponibleResponse>>> listarTodos() {
        List<TurnoDisponibleResponse> turnos = turnoService.listarTurnosDisponibles();
        return ResponseEntity.ok(ApiResponse.ok(turnos));
    }

    /**
     * POST /api/turnos
     * Crea un nuevo turno. Recibe JSON con idMedico, fecha, horaInicio, horaFin.
     * ROL: ADMINISTRADOR
     *
     * Razon: admin.service.ts llama POST /api/turnos con TurnoRequest
     * pero el endpoint no existia.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<TurnoDisponibleResponse>> crearTurno(
            @RequestBody com.clinicaaviva.dto.request.TurnoRequest request) {
        TurnoDisponibleResponse turno = turnoService.crearTurno(request);
        return ResponseEntity.ok(ApiResponse.ok("Turno creado exitosamente", turno));
    }

    /**
     * DELETE /api/turnos/{id}
     * Elimina un turno libre. No permite eliminar turnos ocupados.
     * ROL: ADMINISTRADOR
     *
     * Razon: admin.service.ts llama DELETE /api/turnos/{id} pero
     * el endpoint no existia.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<String>> eliminarTurno(@PathVariable Integer id) {
        turnoService.eliminarTurno(id);
        return ResponseEntity.ok(ApiResponse.ok("Turno eliminado exitosamente"));
    }
}
