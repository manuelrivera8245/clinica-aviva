package com.clinicaaviva.service;

import com.clinicaaviva.dto.request.TurnoRequest;
import com.clinicaaviva.dto.response.TurnoDisponibleResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio de gestion de turnos (slots de tiempo).
 */
public interface TurnoService {

    List<TurnoDisponibleResponse> listarTurnosDisponibles();

    List<TurnoDisponibleResponse> listarTurnosDisponiblesPorEspecialidad(Integer idEspecialidad);

    List<TurnoDisponibleResponse> listarTurnosDisponiblesPorMedico(Integer idMedico, LocalDate fecha);

    void generarTurnosDesdeHorario(Integer idMedico, LocalDate fechaDesde, LocalDate fechaHasta);

    /**
     * Crea un nuevo turno en la agenda de un medico.
     * @throws IllegalArgumentException si el medico no existe o el slot ya existe.
     */
    TurnoDisponibleResponse crearTurno(TurnoRequest request);

    /**
     * Elimina un turno libre por su ID.
     * @throws IllegalArgumentException si el turno no existe o esta ocupado.
     */
    void eliminarTurno(Integer idTurno);
}
