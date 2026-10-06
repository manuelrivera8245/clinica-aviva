package com.clinicaaviva.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * DTO de respuesta para la configuracion general de la clinica.
 * Endpoint publico - se muestra en el landing page.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionGeneralResponse {

    private Integer id;
    private String nombreClinica;
    private String correoContacto;
    private String telefonoContacto;
    private String direccion;
    private LocalTime horarioAperturaGeneral;
    private LocalTime horarioCierreGeneral;
    private Boolean estadoDisponibilidad;
}
