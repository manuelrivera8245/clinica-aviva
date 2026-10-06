package com.clinicaaviva.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa un medico de la clinica.
 * Los medicos tienen una especialidad asignada y atienden citas programadas.
 */
@Entity
@Table(name = "medico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medico {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_medico")
        private Integer idMedico;

        @Column(nullable = false, length = 100)
        private String nombres;

        @Column(nullable = false, length = 100)
        private String apellidos;

        @Column(nullable = false, unique = true, length = 150)
        private String correo;

        @Column(nullable = false, unique = true, length = 50)
        private String usuario;

        @Column(nullable = false, length = 255)
        private String contrasena;

        @Column(nullable = false)
        @Builder.Default
        private Boolean activo = true;

        // Relacion: un medico pertenece a una especialidad
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "id_especialidad", nullable = false, foreignKey = @ForeignKey(name = "fk_medico_especialidad"))
        private Especialidad especialidad;

        // Relacion: un medico es creado por un administrador
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "creado_por", nullable = false, foreignKey = @ForeignKey(name = "fk_medico_admin"))
        private Administrador creadoPor;

        // Relacion: un medico tiene muchos horarios
        @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @Builder.Default
        private List<HorarioMedico> horarios = new ArrayList<>();

        // Relacion: un medico tiene muchos turnos
        @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @Builder.Default
        private List<Turno> turnos = new ArrayList<>();
}
