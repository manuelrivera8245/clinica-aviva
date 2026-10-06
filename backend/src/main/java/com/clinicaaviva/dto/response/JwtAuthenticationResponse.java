package com.clinicaaviva.dto.response;

import com.clinicaaviva.model.enums.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta despues de un login exitoso.
 * Contiene el token JWT, datos del usuario y su rol.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationResponse {

    private String token;
    private String tipo;
    private Integer idUsuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private RolUsuario rol;
}
