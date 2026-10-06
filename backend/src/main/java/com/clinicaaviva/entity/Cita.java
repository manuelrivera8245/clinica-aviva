package com.clinicaaviva.entity;

import com.clinicaaviva.model.enums.EstadoCita;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa una cita medica reservada.
 * Vincula un paciente con un turno especifico y lleva el seguimiento de su
 * estado.
 * La relacion con turno es 1:1 (un turno solo puede tener una cita).
 */
@Entity
@Table(name = "cita")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cita {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_cita")
        private Integer idCita;

        @Column(name = "fecha_reserva", nullable = false, updatable = false)
        @Builder.Default
        private LocalDateTime fechaReserva = LocalDateTime.now();

        // EstadoCitaConverter (autoApply=true) gestiona la conversion BD<->Java.
        // No usar @Enumerated aqui: el converter traduce 'No Asistio' (BD) <-> No_Asistio (Java).
        @Column(nullable = false, length = 20)
        @Builder.Default
        private EstadoCita estado = EstadoCita.Programada;

        // Relacion: una cita pertenece a un paciente
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "id_paciente", nullable = false, foreignKey = @ForeignKey(name = "fk_cita_paciente"))
        private Paciente paciente;

        // Relacion: una cita esta vinculada a un turno (varias citas pueden apuntar al mismo turno históricamente)
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "id_turno", nullable = false, foreignKey = @ForeignKey(name = "fk_cita_turno"))
        private Turno turno;

        // Relacion: una cita genera muchas notificaciones
        @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @Builder.Default
        private List<Notificacion> notificaciones = new ArrayList<>();
}
