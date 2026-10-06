package com.clinicaaviva.service;

import com.clinicaaviva.dto.request.MedicoRequest;
import com.clinicaaviva.dto.response.MedicoResponse;

import java.util.List;

/**
 * Servicio de gestion de medicos.
 */
public interface MedicoService {

    MedicoResponse crearMedico(MedicoRequest request, Integer idAdmin);

    MedicoResponse actualizarMedico(Integer idMedico, MedicoRequest request);

    void darDeBajaMedico(Integer idMedico);

    MedicoResponse obtenerPorId(Integer idMedico);

    List<MedicoResponse> listarTodos();

    List<MedicoResponse> listarPorEspecialidad(Integer idEspecialidad);

    long contarMedicosActivos();
}
