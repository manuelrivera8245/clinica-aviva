package com.clinicaaviva.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para los datos de un paciente.
 * Excluye la contrasena por seguridad.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResponse {

    private Integer idPaciente;
    private String dni;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private LocalDateTime fechaRegistro;
    private Boolean activo;
}
