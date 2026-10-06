package com.clinicaaviva.entity;

import com.clinicaaviva.model.enums.EstadoTurno;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa un slot (turno) especifico en la agenda de un medico.
 * Es la unidad basica de tiempo que puede ser reservada por un paciente.
 * La combinacion (id_medico, fecha, hora_inicio) es unica.
 */
@Entity
@Table(name = "turno",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_turno_slot",
                        columnNames = {"id_medico", "fecha", "hora_inicio"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turno")
    private Integer idTurno;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoTurno estado = EstadoTurno.Libre;

    // Relacion: un turno pertenece a un medico
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medico", nullable = false,
            foreignKey = @ForeignKey(name = "fk_turno_medico"))
    private Medico medico;

    // Relacion: un turno puede tener varias citas en su historial (pero solo 1 activa)
    @OneToMany(mappedBy = "turno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Cita> citas = new ArrayList<>();
}
