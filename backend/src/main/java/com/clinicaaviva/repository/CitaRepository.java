package com.clinicaaviva.repository;

import com.clinicaaviva.entity.Cita;
import com.clinicaaviva.model.enums.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
//import java.util.Map;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Cita.
 * Gestiona las reservas medicas, su historial y los cambios de estado.
 * Incluye consultas equivalentes a las vistas del esquema SQL.
 */
@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

       /**
        * Busca una cita programada especifica de un paciente.
        * Utilizado para validar cancelaciones.
        */
       @Query("SELECT c FROM Cita c JOIN FETCH c.turno t WHERE c.idCita = :idCita AND c.paciente.idPaciente = :idPaciente AND c.estado = 'Programada'")
       Optional<Cita> findCitaProgramadaByIdAndPaciente(@Param("idCita") Integer idCita,
                     @Param("idPaciente") Integer idPaciente);

       /**
        * Lista las citas vigentes (Programadas) de un paciente.
        */
       @Query("SELECT c FROM Cita c JOIN FETCH c.turno t JOIN FETCH t.medico m JOIN FETCH m.especialidad e " +
                     "WHERE c.paciente.idPaciente = :idPaciente AND c.estado = 'Programada' ORDER BY t.fecha, t.horaInicio")
       List<Cita> findCitasProgramadasByPaciente(@Param("idPaciente") Integer idPaciente);

       /**
        * Lista el historial completo de citas de un paciente (todos los estados).
        */
       @Query("SELECT c FROM Cita c JOIN FETCH c.turno t JOIN FETCH t.medico m JOIN FETCH m.especialidad e " +
                     "WHERE c.paciente.idPaciente = :idPaciente ORDER BY t.fecha DESC, t.horaInicio DESC")
       List<Cita> findHistorialByPaciente(@Param("idPaciente") Integer idPaciente);

       /**
        * Lista el historial completo de citas de un medico (todos los estados).
        * BUG FIX D: el medico usaba GET /api/citas/historial (solo PACIENTE) y recibia
        * 401.
        * Se agrega query propia para el historial del medico filtrado por idMedico.
        */
       @Query("SELECT c FROM Cita c JOIN FETCH c.paciente p JOIN FETCH c.turno t JOIN FETCH t.medico m JOIN FETCH m.especialidad e "
                     +
                     "WHERE m.idMedico = :idMedico ORDER BY t.fecha DESC, t.horaInicio DESC")
       List<Cita> findHistorialByMedico(@Param("idMedico") Integer idMedico);

       /**
        * Lista las citas del dia actual para un medico (equivalente a
        * vista_citas_del_dia).
        */
       @Query("SELECT c FROM Cita c JOIN FETCH c.paciente p JOIN FETCH c.turno t " +
                     "JOIN FETCH t.medico m JOIN FETCH m.especialidad e " +
                     "WHERE t.fecha = CURRENT_DATE AND m.idMedico = :idMedico ORDER BY t.horaInicio")
       List<Cita> findCitasDelDiaByMedico(@Param("idMedico") Integer idMedico);

       /**
        * Lista todas las citas del dia actual (panel administrativo).
        */
       @Query("SELECT c FROM Cita c JOIN FETCH c.paciente p JOIN FETCH c.turno t " +
                     "JOIN FETCH t.medico m JOIN FETCH m.especialidad e " +
                     "WHERE t.fecha = CURRENT_DATE ORDER BY t.horaInicio")
       List<Cita> findCitasDelDia();

       /**
        * Lista citas en un rango de fechas para reportes.
        * Usa JOIN FETCH para cargar paciente y medico en una sola query,
        * evitando que mapToResponse() devuelva campos null.
        * Razon del cambio (R-03): la query original sin FETCH dejaba
        * nombrePaciente y nombreMedico como null en los reportes del admin.
        */
       @Query("SELECT c FROM Cita c " +
                     "JOIN FETCH c.paciente p " +
                     "JOIN FETCH c.turno t " +
                     "JOIN FETCH t.medico m " +
                     "JOIN FETCH m.especialidad e " +
                     "WHERE t.fecha BETWEEN :desde AND :hasta " +
                     "ORDER BY t.fecha, t.horaInicio")
       List<Cita> findCitasByRangoFecha(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

       /**
        * Cuenta citas por estado de un medico en una fecha.
        */
       @Query("SELECT COUNT(c) FROM Cita c JOIN c.turno t WHERE t.medico.idMedico = :idMedico AND t.fecha = :fecha AND c.estado = :estado")
       long countByMedicoAndFechaAndEstado(@Param("idMedico") Integer idMedico, @Param("fecha") LocalDate fecha,
                     @Param("estado") EstadoCita estado);

       /**
        * Verifica si un paciente tiene citas programadas.
        */
       boolean existsByPacienteIdPacienteAndEstado(Integer idPaciente, EstadoCita estado);

       /**
        * Cuenta el total global de citas (todos los estados).
        * Usado para calcular tasaAusentismo en el dashboard.
        */
       @Query("SELECT COUNT(c) FROM Cita c WHERE c.estado = :estado")
       long countByEstado(@Param("estado") EstadoCita estado);

       /**
        * Cuenta citas de hoy (todos los estados).
        */
       @Query("SELECT COUNT(c) FROM Cita c JOIN c.turno t WHERE t.fecha = CURRENT_DATE")
       long countCitasHoy();
}
