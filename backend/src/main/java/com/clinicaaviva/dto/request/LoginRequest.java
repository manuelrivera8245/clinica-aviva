package com.clinicaaviva.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de inicio de sesion.
 * Acepta correo electronico, DNI o nombre de usuario como credencial.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "La credencial es obligatoria")
    private String credencial;

    @NotBlank(message = "La contrasena es obligatoria")
    private String contrasena;
}
