package com.clinicaaviva.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * DTO para actualizar la configuracion general de la clinica.
 * Solo accesible por el rol ADMINISTRADOR.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionGeneralRequest {

    @NotBlank(message = "El nombre de la clinica es obligatorio")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
    private String nombreClinica;

    @NotBlank(message = "El correo de contacto es obligatorio")
    @Email(message = "El formato del correo no es valido")
    @Size(max = 150, message = "El correo no puede exceder 150 caracteres")
    private String correoContacto;

    @NotBlank(message = "El telefono de contacto es obligatorio")
    @Size(max = 20, message = "El telefono no puede exceder 20 caracteres")
    private String telefonoContacto;

    @NotBlank(message = "La direccion es obligatoria")
    @Size(max = 255, message = "La direccion no puede exceder 255 caracteres")
    private String direccion;

    @NotNull(message = "El horario de apertura es obligatorio")
    private LocalTime horarioAperturaGeneral;

    @NotNull(message = "El horario de cierre es obligatorio")
    private LocalTime horarioCierreGeneral;

    @NotNull(message = "El estado de disponibilidad es obligatorio")
    @Builder.Default
    private Boolean estadoDisponibilidad = true;
}
