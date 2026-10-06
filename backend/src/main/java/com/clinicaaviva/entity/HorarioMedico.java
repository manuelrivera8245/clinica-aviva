package com.clinicaaviva.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

/**
 * Entidad JPA que representa el horario de atencion de un medico en un dia especifico.
 * Define los rangos de horas y la duracion de cada turno (slot) de atencion.
 */
@Entity
@Table(name = "horario_medico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Integer idHorario;

    @Column(name = "dia_semana", nullable = false)
    private Integer diaSemana; // 1=Lunes, 2=Martes, ..., 7=Domingo

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "duracion_turno_min", nullable = false)
    @Builder.Default
    private Integer duracionTurnoMin = 30;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    // Relacion: un horario pertenece a un medico
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medico", nullable = false,
            foreignKey = @ForeignKey(name = "fk_horario_medico"))
    private Medico medico;

    // Relacion: un horario es creado por un administrador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por", nullable = false,
            foreignKey = @ForeignKey(name = "fk_horario_admin"))
    private Administrador creadoPor;
}
