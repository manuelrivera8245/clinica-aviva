package com.clinicaaviva.service;

import com.clinicaaviva.dto.request.ActualizarPerfilPacienteRequest;
import com.clinicaaviva.dto.response.PacienteResponse;

import java.util.List;

/**
 * Servicio de gestion de pacientes.
 */
public interface PacienteService {

    PacienteResponse obtenerPerfil(Integer idPaciente);

    PacienteResponse actualizarPerfil(Integer idPaciente, ActualizarPerfilPacienteRequest request);

    List<PacienteResponse> listarTodos();

    long contarPacientes();

    /**
     * Desactiva un paciente (baja logica). Preserva historial de citas.
     * @throws IllegalArgumentException si el paciente no existe
     */
    void desactivarPaciente(Integer idPaciente);
}
