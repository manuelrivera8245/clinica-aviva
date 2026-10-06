package com.clinicaaviva.entity;

import com.clinicaaviva.model.enums.EstadoEnvio;
import com.clinicaaviva.model.enums.TipoNotificacion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una notificacion automatica del sistema.
 * El sistema genera notificaciones de tipo Confirmacion, Recordatorio (24h antes)
 * y Cancelacion que deben ser enviadas por correo electronico.
 */
@Entity
@Table(name = "notificacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Integer idNotificacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoNotificacion tipo;

    @Column(name = "fecha_programada", nullable = false)
    private LocalDateTime fechaProgramada;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_envio", nullable = false, length = 20)
    @Builder.Default
    private EstadoEnvio estadoEnvio = EstadoEnvio.Pendiente;

    @Column(name = "leido", nullable = false)
    @Builder.Default
    private Boolean leido = false;

    // Relacion: una notificacion pertenece a una cita
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cita", nullable = false,
            foreignKey = @ForeignKey(name = "fk_notif_cita"))
    private Cita cita;
}
