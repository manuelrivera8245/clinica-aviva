package com.clinicaaviva.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para una especialidad medica.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EspecialidadResponse {

    private Integer idEspecialidad;
    private String nombre;
    private String descripcion;
    private Boolean activo;
}
