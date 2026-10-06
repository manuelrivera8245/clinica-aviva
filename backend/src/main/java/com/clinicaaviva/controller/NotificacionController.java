package com.clinicaaviva.controller;

import com.clinicaaviva.dto.response.ApiResponse;
import com.clinicaaviva.dto.response.NotificacionResponse;
import com.clinicaaviva.security.UserPrincipal;
import com.clinicaaviva.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping("/mis-notificaciones")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<ApiResponse<List<NotificacionResponse>>> obtenerMisNotificaciones(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        List<NotificacionResponse> notificaciones = notificacionService.obtenerMisNotificaciones(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.ok(notificaciones));
    }

    @PutMapping("/marcar-leidas")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<ApiResponse<String>> marcarComoLeidas(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        notificacionService.marcarComoLeidas(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.ok("Notificaciones marcadas como leídas", "Éxito"));
    }
}
