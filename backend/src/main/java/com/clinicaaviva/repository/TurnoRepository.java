package com.clinicaaviva.repository;

import com.clinicaaviva.entity.Turno;
import com.clinicaaviva.model.enums.EstadoTurno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Turno.
 * Gestiona los slots de tiempo disponibles para citas medicas,
 * incluyendo bloqueo pesimista para reservas concurrentes.
 */
@Repository
public interface TurnoRepository extends JpaRepository<Turno, Integer> {

    /**
     * Busca un turno por ID con bloqueo pesimista (PESSIMISTIC_WRITE).
     * Utilizado durante la reserva de citas para evitar condiciones de carrera.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Turno t WHERE t.idTurno = :id")
    Optional<Turno> findByIdWithLock(@Param("id") Integer id);

    /**
     * Lista turnos libres de un medico en una fecha especifica.
     */
    List<Turno> findByMedicoIdMedicoAndFechaAndEstado(
            Integer idMedico, LocalDate fecha, EstadoTurno estado);

    /**
     * Lista turnos libres de un medico en una fecha exacta.
     */
    @Query("SELECT t FROM Turno t JOIN FETCH t.medico m JOIN FETCH m.especialidad e " +
           "WHERE m.idMedico = :idMedico AND t.fecha = :fecha AND t.estado = 'Libre' " +
           "ORDER BY t.horaInicio")
    List<Turno> findTurnosLibresByMedicoAndFecha(
            @Param("idMedico") Integer idMedico,
            @Param("fecha") LocalDate fecha);

    /**
     * Lista turnos libres a partir de hoy para un medico especifico.
     * BUG FIX: agregado JOIN FETCH t.medico m JOIN FETCH m.especialidad e
     * para evitar LazyInitializationException al acceder a t.getMedico().getEspecialidad()
     * en mapToResponse() cuando open-in-view=false (Spring Boot 3 por defecto).
     */
    @Query("SELECT t FROM Turno t JOIN FETCH t.medico m JOIN FETCH m.especialidad e " +
           "WHERE m.idMedico = :idMedico AND t.fecha >= :fecha AND t.estado = 'Libre' " +
           "ORDER BY t.fecha, t.horaInicio")
    List<Turno> findTurnosLibresByMedicoDesdeFecha(
            @Param("idMedico") Integer idMedico,
            @Param("fecha") LocalDate fecha);

    /**
     * Lista todos los turnos libres a partir de hoy con informacion del medico.
     * Equivalente a la vista: vista_turnos_disponibles
     */
    @Query("SELECT t FROM Turno t JOIN FETCH t.medico m JOIN FETCH m.especialidad e " +
           "WHERE t.estado = 'Libre' AND t.fecha >= :fecha AND m.activo = true " +
           "ORDER BY t.fecha, t.horaInicio")
    List<Turno> findTurnosDisponibles(@Param("fecha") LocalDate fecha);

    /**
     * Lista turnos libres filtrados por especialidad.
     */
    @Query("SELECT t FROM Turno t JOIN FETCH t.medico m JOIN FETCH m.especialidad e " +
           "WHERE t.estado = 'Libre' AND t.fecha >= :fecha AND m.activo = true AND e.idEspecialidad = :idEspecialidad " +
           "ORDER BY t.fecha, t.horaInicio")
    List<Turno> findTurnosDisponiblesByEspecialidad(
            @Param("idEspecialidad") Integer idEspecialidad,
            @Param("fecha") LocalDate fecha);

    /**
     * Lista los turnos de un medico en una fecha especifica (todos los estados).
     */
    List<Turno> findByMedicoIdMedicoAndFechaOrderByHoraInicio(Integer idMedico, LocalDate fecha);

    /**
     * Cuenta los turnos ocupados de un medico en una fecha.
     */
    long countByMedicoIdMedicoAndFechaAndEstado(Integer idMedico, LocalDate fecha, EstadoTurno estado);

    /**
     * Comprueba si ya existe un turno para ese médico, fecha y hora de inicio.
     * Usado en generarTurnosDesdeHorario para evitar duplicados sin lanzar excepción.
     */
    boolean existsByMedicoIdMedicoAndFechaAndHoraInicio(Integer idMedico, LocalDate fecha, java.time.LocalTime horaInicio);
}
