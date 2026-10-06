package com.clinicaaviva.controller;

import com.clinicaaviva.dto.request.ActualizarEstadoCitaRequest;
import com.clinicaaviva.dto.request.CancelacionCitaRequest;
import com.clinicaaviva.dto.request.ReservaCitaRequest;
import com.clinicaaviva.dto.response.ApiResponse;
import com.clinicaaviva.dto.response.CitaResponse;
import com.clinicaaviva.security.UserPrincipal;
import com.clinicaaviva.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

/**
 * Controlador REST para la gestion de citas medicas.
 * - Pacientes: reservar, cancelar, ver sus citas e historial
 * - Medicos: ver agenda del dia, actualizar estado de citas
 * - Administradores: monitor de citas del dia
 */
@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CitaController {

    private final CitaService citaService;

    /**
     * POST /api/citas/reservar
     * Reserva una cita (RF-04). Bloquea el turno transaccionalmente.
     * ROL: PACIENTE
     */
    @PostMapping("/reservar")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<ApiResponse<String>> reservarCita(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ReservaCitaRequest request) {
        String resultado = citaService.reservarCita(userPrincipal.getId(), request);
        if ("RESERVA_EXITOSA".equals(resultado)) {
            return ResponseEntity.ok(ApiResponse.ok("Cita reservada exitosamente", resultado));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("No se pudo reservar: " + resultado));
    }

    /**
     * POST /api/citas/cancelar
     * Cancela una cita con validacion de 24h de anticipacion (RF-05).
     * ROL: PACIENTE
     */
    @PostMapping("/cancelar")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<ApiResponse<String>> cancelarCita(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CancelacionCitaRequest request) {
        String resultado = citaService.cancelarCita(userPrincipal.getId(), request);
        if ("CANCELACION_EXITOSA".equals(resultado)) {
            return ResponseEntity.ok(ApiResponse.ok("Cita cancelada exitosamente", resultado));
        } else if ("FUERA_DE_PLAZO".equals(resultado)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("No puede cancelar con menos de 24 horas de anticipacion"));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("Cita no encontrada"));
    }

    /**
     * PUT /api/citas/actualizar-estado
     * El medico marca una cita como "Atendida" o "No Asistio" (RF-09).
     * ROL: MEDICO
     */
    @PutMapping("/actualizar-estado")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<ApiResponse<String>> actualizarEstado(
            @Valid @RequestBody ActualizarEstadoCitaRequest request) {
        String resultado = citaService.actualizarEstadoCita(request);
        if ("ACTUALIZADO".equals(resultado)) {
            return ResponseEntity.ok(ApiResponse.ok("Estado actualizado correctamente", resultado));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("No se pudo actualizar el estado"));
    }

    /**
     * GET /api/citas/mis-citas
     * Lista las citas programadas del paciente autenticado.
     * ROL: PACIENTE
     */
    @GetMapping("/mis-citas")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<ApiResponse<List<CitaResponse>>> listarMisCitasProgramadas(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<CitaResponse> citas = citaService.listarMisCitasProgramadas(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.ok(citas));
    }

    /**
     * GET /api/citas/historial
     * Historial completo de citas del paciente (todos los estados).
     * ROL: PACIENTE
     */
    @GetMapping("/historial")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<ApiResponse<List<CitaResponse>>> listarHistorial(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<CitaResponse> citas = citaService.listarHistorialPaciente(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.ok(citas));
    }

    /**
     * GET /api/citas/agenda-dia
     * Agenda del dia para el medico autenticado.
     * ROL: MEDICO
     */
    @GetMapping("/agenda-dia")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<ApiResponse<List<CitaResponse>>> listarAgendaDelDia(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<CitaResponse> citas = citaService.listarAgendaDelDia(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.ok(citas));
    }



    /**
     * GET /api/citas/historial-medico
     * Historial completo de citas del medico autenticado (todos los estados).
     * ROL: MEDICO
     *
     * BUG FIX D: el frontend del medico llamaba GET /api/citas/historial
     * que tiene hasRole('PACIENTE'). Spring Security rechazaba con 401
     * (el medico tiene token valido pero no tiene rol PACIENTE).
     * Se crea un endpoint propio para el medico en /historial-medico.
     */
    @GetMapping("/historial-medico")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<ApiResponse<List<CitaResponse>>> listarHistorialMedico(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<CitaResponse> historial = citaService.listarHistorialMedico(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.ok("Historial de citas del medico", historial));
    }

    /**
     * GET /api/citas/{id}/comprobante
     * Genera y descarga el comprobante en PDF de una cita (RF-06).
     * ROL: PACIENTE
     */
    @GetMapping("/{id}/comprobante")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<byte[]> descargarComprobante(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Integer id) {
        
        byte[] pdfBytes = citaService.generarComprobantePdf(id, userPrincipal.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "comprobante_cita_" + id + ".pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
