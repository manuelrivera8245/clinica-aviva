package com.clinicaaviva.controller;

import com.clinicaaviva.dto.request.ActualizarPerfilPacienteRequest;
import com.clinicaaviva.dto.response.ApiResponse;
import com.clinicaaviva.dto.response.PacienteResponse;
import com.clinicaaviva.security.UserPrincipal;
import com.clinicaaviva.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestion de pacientes.
 */
@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PacienteController {

    private final PacienteService pacienteService;

    /**
     * GET /api/pacientes/perfil
     * Obtiene el perfil del paciente autenticado.
     * ROL: PACIENTE
     */
    @GetMapping("/perfil")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<ApiResponse<PacienteResponse>> obtenerPerfil(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        PacienteResponse perfil = pacienteService.obtenerPerfil(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.ok(perfil));
    }

    /**
     * PUT /api/pacientes/perfil
     * Actualiza los datos del paciente autenticado.
     * ROL: PACIENTE
     *
     * BUG FIX: el endpoint no existia. paciente.service.ts lo llamaba con
     * PUT /api/pacientes/perfil y recibia 404 ("No static resource").
     * Solo se permiten modificar: nombres, apellidos, correo, telefono, contrasena.
     * El DNI es inmutable (identificador unico del paciente).
     */
    @PutMapping("/perfil")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<ApiResponse<PacienteResponse>> actualizarPerfil(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ActualizarPerfilPacienteRequest request) {
        PacienteResponse perfil = pacienteService.actualizarPerfil(userPrincipal.getId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Perfil actualizado exitosamente", perfil));
    }

    /**
     * GET /api/pacientes
     * Lista todos los pacientes registrados.
     * ROL: ADMINISTRADOR
     *
     * Razon: admin.service.ts llama GET /api/pacientes para la pantalla
     * de gestion de pacientes, pero el endpoint no existia.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<List<PacienteResponse>>> listarTodos() {
        List<PacienteResponse> pacientes = pacienteService.listarTodos();
        return ResponseEntity.ok(ApiResponse.ok(pacientes));
    }

    /**
     * DELETE /api/pacientes/{id}
     * Desactiva (baja logica) a un paciente por su ID.
     * ROL: ADMINISTRADOR
     *
     * Razon: admin.service.ts llama DELETE /api/pacientes/{id} pero
     * el endpoint no existia. Se implementa como baja logica (activo=false)
     * en lugar de borrado fisico para preservar el historial de citas.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<String>> desactivarPaciente(@PathVariable Integer id) {
        pacienteService.desactivarPaciente(id);
        return ResponseEntity.ok(ApiResponse.ok("Paciente desactivado exitosamente"));
    }
}

