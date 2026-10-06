package com.clinicaaviva.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entidad JPA que almacena la informacion general y configuracion dinamica
 * de la Clinica Aviva. Contiene un unico registro que el administrador
 * puede modificar para reflejar cambios en horarios, contacto o estado.
 *
 * Tabla: configuracion_general
 */
@Entity
@Table(name = "configuracion_general")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracionGeneral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_clinica", nullable = false, length = 150)
    private String nombreClinica;

    @Column(name = "correo_contacto", nullable = false, length = 150)
    private String correoContacto;

    @Column(name = "telefono_contacto", nullable = false, length = 20)
    private String telefonoContacto;

    @Column(nullable = false, length = 255)
    private String direccion;

    @Column(name = "horario_apertura_general", nullable = false)
    private LocalTime horarioAperturaGeneral;

    @Column(name = "horario_cierre_general", nullable = false)
    private LocalTime horarioCierreGeneral;

    @Column(name = "estado_disponibilidad", nullable = false)
    @Builder.Default
    private Boolean estadoDisponibilidad = true;

    @Column(name = "fecha_actualizacion", nullable = false)
    @Builder.Default
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
}
