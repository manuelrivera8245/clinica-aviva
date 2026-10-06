package com.clinicaaviva.repository;

import com.clinicaaviva.entity.Notificacion;
import com.clinicaaviva.model.enums.EstadoCita;
import com.clinicaaviva.model.enums.EstadoEnvio;
import com.clinicaaviva.model.enums.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para la entidad Notificacion.
 * Gestiona la cola de notificaciones pendientes de envio
 * y el historial de envios realizados.
 */
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

       /**
        * Lista notificaciones pendientes cuya fecha programada ya llego.
        * Equivalente a la vista: vista_notificaciones_pendientes
        */
       @Query("SELECT n FROM Notificacion n JOIN FETCH n.cita c JOIN FETCH c.paciente p " +
                     "JOIN FETCH c.turno t JOIN FETCH t.medico m JOIN FETCH m.especialidad e " +
                     "WHERE n.estadoEnvio = 'Pendiente' AND n.fechaProgramada <= :ahora AND c.estado = 'Programada'")
       List<Notificacion> findNotificacionesPendientes(@Param("ahora") LocalDateTime ahora);

       /**
        * Lista notificaciones de una cita especifica.
        */
       List<Notificacion> findByCitaIdCita(Integer idCita);

       /**
        * Lista notificaciones por estado de envio.
        */
       List<Notificacion> findByEstadoEnvio(EstadoEnvio estadoEnvio);

       /**
        * Cuenta notificaciones pendientes.
        */
       long countByEstadoEnvio(EstadoEnvio estadoEnvio);

       /**
        * Lista notificaciones de un paciente para su bandeja (campanita).
        * Solo muestra notificaciones cuya fecha ya pasó (fechaProgramada <= ahora).
        * Si la cita fue cancelada, no muestra el Recordatorio.
        */
       @Query("SELECT n FROM Notificacion n JOIN FETCH n.cita c JOIN FETCH c.paciente p " +
              "JOIN FETCH c.turno t JOIN FETCH t.medico m JOIN FETCH m.especialidad e " +
              "WHERE p.idPaciente = :idPaciente " +
              "AND n.fechaProgramada <= :ahora " +
              "AND (n.tipo != :tipoRecordatorio OR c.estado = :estadoProgramada) " +
              "ORDER BY n.fechaProgramada DESC")
       List<Notificacion> findMisNotificaciones(
               @Param("idPaciente") Integer idPaciente, 
               @Param("ahora") LocalDateTime ahora,
               @Param("tipoRecordatorio") TipoNotificacion tipoRecordatorio,
               @Param("estadoProgramada") EstadoCita estadoProgramada);

       /**
        * Marca todas las notificaciones no leídas de un paciente como leídas.
        */
       @Modifying
       @Query("UPDATE Notificacion n SET n.leido = true WHERE n.cita.paciente.idPaciente = :idPaciente AND n.leido = false")
       void marcarComoLeidasPorPaciente(@Param("idPaciente") Integer idPaciente);
}
