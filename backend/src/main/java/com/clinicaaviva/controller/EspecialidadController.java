package com.clinicaaviva.controller;

import com.clinicaaviva.dto.response.ApiResponse;
import com.clinicaaviva.dto.response.EspecialidadResponse;
import com.clinicaaviva.service.EspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestion de especialidades medicas.
 * - GET: Publico (listar especialidades para reserva)
 * - POST/Admin: Solo administradores (crear nuevas especialidades)
 */
@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    /**
     * GET /api/especialidades
     * Lista todas las especialidades activas.
     * PUBLICO: Usado en el flujo de reserva de citas.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<EspecialidadResponse>>> listarActivas() {
        List<EspecialidadResponse> especialidades = especialidadService.listarActivas();
        return ResponseEntity.ok(ApiResponse.ok(especialidades));
    }

    /**
     * GET /api/especialidades/{id}
     * Obtiene una especialidad por ID.
     * PUBLICO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EspecialidadResponse>> obtenerPorId(@PathVariable Integer id) {
        EspecialidadResponse especialidad = especialidadService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.ok(especialidad));
    }

    /**
     * POST /api/especialidades
     * Crea una nueva especialidad medica.
     * ROL: ADMINISTRADOR
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<EspecialidadResponse>> crear(
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion) {
        EspecialidadResponse especialidad = especialidadService.crear(nombre, descripcion);
        return ResponseEntity.ok(ApiResponse.ok("Especialidad creada exitosamente", especialidad));
    }
}
