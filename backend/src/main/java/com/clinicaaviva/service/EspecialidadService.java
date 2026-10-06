package com.clinicaaviva.service;

import com.clinicaaviva.dto.response.EspecialidadResponse;

import java.util.List;

/**
 * Servicio de gestion de especialidades medicas.
 */
public interface EspecialidadService {

    List<EspecialidadResponse> listarActivas();

    EspecialidadResponse obtenerPorId(Integer id);

    EspecialidadResponse crear(String nombre, String descripcion);
}
