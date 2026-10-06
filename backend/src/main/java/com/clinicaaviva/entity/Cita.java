package com.clinicaaviva.entity;

import com.clinicaaviva.model.enums.EstadoCita;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

        // El converter (autoApply=true) traduce 'No Asistio' (BD) <-> No_Asistio (Java).
        // No usar @Enumerated aqui para evitar conflicto con el converter.
        @Column(nullable = false, length = 20)
        @Builder.Default
        private EstadoCita estado = EstadoCita.Programada;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "id_paciente", nullable = false, foreignKey = @ForeignKey(name = "fk_cita_paciente"))
        private Paciente paciente;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "id_turno", nullable = false, foreignKey = @ForeignKey(name = "fk_cita_turno"))
        private Turno turno;

        @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @Builder.Default
        private List<Notificacion> notificaciones = new ArrayList<>();
}
