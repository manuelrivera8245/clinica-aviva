package com.clinicaaviva.service;

import com.clinicaaviva.dto.request.HorarioMedicoRequest;
import com.clinicaaviva.dto.response.HorarioMedicoResponse;

import java.util.List;

/**
 * Servicio para la gestión de horarios de atención de médicos (RF-11).
 */
public interface HorarioMedicoService {

    /** Lista los horarios activos de un médico específico. */
    List<HorarioMedicoResponse> listarPorMedico(Integer idMedico);

    /** Crea un nuevo horario para un médico. */
    HorarioMedicoResponse crear(HorarioMedicoRequest request, Integer idAdministrador);

    /** Desactiva (borrado lógico) un horario por su ID. */
    void eliminar(Integer idHorario);
}
