package com.clinicaaviva.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear o actualizar un medico (RF-10).
 * Solo los administradores pueden gestionar medicos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicoRequest {

    private Integer idMedico;

    @NotNull(message = "La especialidad es obligatoria")
    private Integer idEspecialidad;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden exceder 100 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden exceder 100 caracteres")
    private String apellidos;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo no es valido")
    @Size(max = 150, message = "El correo no puede exceder 150 caracteres")
    private String correo;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 50, message = "El usuario no puede exceder 50 caracteres")
    private String usuario;

    // BUG FIX B: @Size(min=6) rechazaba contrasena="" (string vacio, longitud 0 < 6)
    // cuando el admin edita un medico sin cambiar la contrasena.
    // @Pattern(regexp = "^$|.{6,}") permite:
    //   - null (campo ausente en el JSON) → valido, el servicio no cambia la contrasena
    //   - "" (string vacio)              → valido, el servicio lo ignora tambien
    //   - cualquier string de 6+ chars  → valido, el servicio encripta y actualiza
    @Pattern(
        regexp = "^$|.{6,}",
        message = "La contrasena debe tener al menos 6 caracteres"
    )
    private String contrasena;
}
