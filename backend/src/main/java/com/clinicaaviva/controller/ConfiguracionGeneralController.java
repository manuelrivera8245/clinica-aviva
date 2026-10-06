package com.clinicaaviva.controller;

import com.clinicaaviva.dto.request.ConfiguracionGeneralRequest;
import com.clinicaaviva.dto.response.ApiResponse;
import com.clinicaaviva.dto.response.ConfiguracionGeneralResponse;
import com.clinicaaviva.service.ConfiguracionGeneralService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la configuracion general de la clinica.
 *
 * - GET /api/configuracion : PUBLICO - Muestra info de la clinica en landing page
 * - PUT /api/configuracion : ADMINISTRADOR - Permite editar la configuracion dinamicamente
 */
@RestController
@RequestMapping("/api/configuracion")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ConfiguracionGeneralController {

    private final ConfiguracionGeneralService configuracionService;

    /**
     * GET /api/configuracion
     * Obtiene la informacion general de la clinica.
     * ACCESO: PUBLICO (sin autenticacion)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<ConfiguracionGeneralResponse>> obtenerConfiguracion() {
        ConfiguracionGeneralResponse config = configuracionService.obtenerConfiguracion();
        return ResponseEntity.ok(ApiResponse.ok(config));
    }

    /**
     * PUT /api/configuracion
     * Actualiza la configuracion general de la clinica.
     * ACCESO: SOLO ADMINISTRADOR
     */
    @PutMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<ConfiguracionGeneralResponse>> actualizarConfiguracion(
            @Valid @RequestBody ConfiguracionGeneralRequest request) {
        ConfiguracionGeneralResponse config = configuracionService.actualizarConfiguracion(request);
        return ResponseEntity.ok(ApiResponse.ok("Configuracion actualizada exitosamente", config));
    }
}
