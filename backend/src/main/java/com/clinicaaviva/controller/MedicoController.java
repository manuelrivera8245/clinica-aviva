package com.clinicaaviva.controller;

import com.clinicaaviva.dto.request.MedicoRequest;
import com.clinicaaviva.dto.response.ApiResponse;
import com.clinicaaviva.dto.response.MedicoResponse;
import com.clinicaaviva.security.UserPrincipal;
import com.clinicaaviva.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestion de medicos.
 * - Admin: CRUD completo de medicos (RF-10)
 * - Publico: Listar medicos activos para reserva de citas
 */
@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class MedicoController {

    private final MedicoService medicoService;

    /**
     * POST /api/medicos
     * Crea un nuevo medico en el sistema.
     * ROL: ADMINISTRADOR
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<MedicoResponse>> crearMedico(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody MedicoRequest request) {
        MedicoResponse medico = medicoService.crearMedico(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.ok("Medico creado exitosamente", medico));
    }

    /**
     * PUT /api/medicos/{id}
     * Actualiza los datos de un medico.
     * ROL: ADMINISTRADOR
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<MedicoResponse>> actualizarMedico(
            @PathVariable Integer id,
            @Valid @RequestBody MedicoRequest request) {
        MedicoResponse medico = medicoService.actualizarMedico(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Medico actualizado exitosamente", medico));
    }

    /**
     * DELETE /api/medicos/{id}
     * Da de baja (borrado logico) a un medico.
     * ROL: ADMINISTRADOR
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Void>> darDeBajaMedico(@PathVariable Integer id) {
        medicoService.darDeBajaMedico(id);
        return ResponseEntity.ok(ApiResponse.ok("Medico dado de baja exitosamente", null));
    }



    /**
     * GET /api/medicos
     * Lista todos los medicos activos.
     * ROL: ADMINISTRADOR
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<List<MedicoResponse>>> listarTodos() {
        List<MedicoResponse> medicos = medicoService.listarTodos();
        return ResponseEntity.ok(ApiResponse.ok(medicos));
    }

    /**
     * GET /api/medicos/especialidad/{idEspecialidad}
     * Lista medicos por especialidad (para filtrado en reserva).
     * ROL: PUBLICO (usado en el flujo de reserva)
     */
    @GetMapping("/especialidad/{idEspecialidad}")
    public ResponseEntity<ApiResponse<List<MedicoResponse>>> listarPorEspecialidad(
            @PathVariable Integer idEspecialidad) {
        List<MedicoResponse> medicos = medicoService.listarPorEspecialidad(idEspecialidad);
        return ResponseEntity.ok(ApiResponse.ok(medicos));
    }

    /**
     * GET /api/medicos/perfil
     * Obtiene el perfil del medico autenticado.
     * ROL: MEDICO
     */
    @GetMapping("/perfil")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<ApiResponse<MedicoResponse>> obtenerPerfil(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        MedicoResponse medico = medicoService.obtenerPorId(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.ok(medico));
    }
}
