package com.clinicaaviva.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar el perfil de un paciente (PUT /api/pacientes/perfil).
 * Solo permite modificar los campos que el paciente puede cambiar por sí mismo.
 * DNI y correo son inmutables (identificadores únicos del sistema).
 *
 * BUG FIX: el endpoint PUT /api/pacientes/perfil no existía.
 * El frontend (paciente.service.ts) lo llamaba y recibía 404.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarPerfilPacienteRequest {

    @Size(max = 100, message = "Los nombres no pueden exceder 100 caracteres")
    private String nombres;

    @Size(max = 100, message = "Los apellidos no pueden exceder 100 caracteres")
    private String apellidos;

    @Email(message = "El formato del correo no es valido")
    @Size(max = 150, message = "El correo no puede exceder 150 caracteres")
    private String correo;

    @Size(max = 15, message = "El telefono no puede exceder 15 caracteres")
    private String telefono;

    @Size(min = 6, message = "La nueva contrasena debe tener al menos 6 caracteres")
    private String contrasena;
}
