package com.clinicaaviva.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para los datos de un medico.
 * Excluye la contrasena por seguridad.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicoResponse {

    private Integer idMedico;
    private String nombres;
    private String apellidos;
    private String correo;
    private String usuario;
    private Boolean activo;
    private EspecialidadResponse especialidad;
}
