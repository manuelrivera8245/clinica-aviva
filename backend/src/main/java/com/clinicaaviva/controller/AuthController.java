package com.clinicaaviva.controller;

import com.clinicaaviva.dto.request.LoginRequest;
import com.clinicaaviva.dto.request.RegistroPacienteRequest;
import com.clinicaaviva.dto.response.ApiResponse;
import com.clinicaaviva.dto.response.JwtAuthenticationResponse;
import com.clinicaaviva.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para autenticacion y registro de usuarios.
 * Endpoints publicos - no requieren autenticacion previa.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/login
     * Autentica un usuario (paciente, medico o administrador) y retorna un JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtAuthenticationResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        JwtAuthenticationResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login exitoso", response));
    }

    /**
     * POST /api/auth/registro
     * Registra un nuevo paciente en el sistema (RF-01).
     * El paciente recibe un JWT para autenticacion inmediata.
     */
    @PostMapping("/registro")
    public ResponseEntity<ApiResponse<JwtAuthenticationResponse>> registrarPaciente(
            @Valid @RequestBody RegistroPacienteRequest request) {
        JwtAuthenticationResponse response = authService.registrarPaciente(request);
        return ResponseEntity.ok(ApiResponse.ok("Paciente registrado exitosamente", response));
    }

    /**
     * GET /api/auth/verificar?credencial={valor}
     * Verifica si una credencial (DNI, correo o usuario) ya esta en uso.
     */
    @GetMapping("/verificar")
    public ResponseEntity<ApiResponse<Boolean>> verificarCredencial(
            @RequestParam String credencial) {
        boolean existe = authService.existeCredencial(credencial);
        return ResponseEntity.ok(ApiResponse.ok(existe));
    }
}
