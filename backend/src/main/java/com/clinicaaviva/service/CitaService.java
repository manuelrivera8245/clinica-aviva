package com.clinicaaviva.service;

import com.clinicaaviva.dto.request.ActualizarEstadoCitaRequest;
import com.clinicaaviva.dto.request.CancelacionCitaRequest;
import com.clinicaaviva.dto.request.ReservaCitaRequest;
import com.clinicaaviva.dto.response.CitaResponse;

import java.util.List;

/**
 * Servicio de gestion de citas medicas.
 * Implementa la logica transaccional de reserva y cancelacion
 * emulando los procedimientos almacenados del esquema SQL.
 */
public interface CitaService {

    /**
     * Reserva una cita con bloqueo pesimista del turno.
     * Previene condiciones de carrera entre multiples usuarios.
     */
    String reservarCita(Integer idPaciente, ReservaCitaRequest request);

    /**
     * Cancela una cita validando el plazo minimo de 24 horas.
     */
    String cancelarCita(Integer idPaciente, CancelacionCitaRequest request);

    /**
     * Actualiza el estado de una cita a "Atendida" o "No Asistio" (solo medico).
     */
    String actualizarEstadoCita(ActualizarEstadoCitaRequest request);

    /**
     * Lista las citas programadas del paciente autenticado.
     */
    List<CitaResponse> listarMisCitasProgramadas(Integer idPaciente);

    /**
     * Lista el historial completo de citas del paciente.
     */
    List<CitaResponse> listarHistorialPaciente(Integer idPaciente);

    /**
     * Lista las citas del dia para un medico (panel del medico).
     */
    List<CitaResponse> listarAgendaDelDia(Integer idMedico);

    /**
     * Lista todas las citas del dia (panel administrativo).
     */
    List<CitaResponse> listarCitasDelDiaAdmin();

    /**
     * Lista el historial completo de citas de un medico (todos los estados).
     * BUG FIX D: el medico usaba GET /api/citas/historial (rol PACIENTE) y recibia 401.
     */
    List<CitaResponse> listarHistorialMedico(Integer idMedico);

    /**
     * Genera el comprobante PDF de una cita especifica para un paciente.
     * RF-06: Comprobante PDF
     */
    byte[] generarComprobantePdf(Integer idCita, Integer idPaciente);
}
