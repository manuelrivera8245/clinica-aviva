package com.clinicaaviva.service.impl;

import com.clinicaaviva.dto.response.EspecialidadResponse;
import com.clinicaaviva.entity.Especialidad;
import com.clinicaaviva.repository.EspecialidadRepository;
import com.clinicaaviva.service.EspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EspecialidadServiceImpl implements EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EspecialidadResponse> listarActivas() {
        return especialidadRepository.findByActivoTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EspecialidadResponse obtenerPorId(Integer id) {
        Especialidad esp = especialidadRepository.findById(java.util.Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));
        return mapToResponse(esp);
    }

    @Override
    @Transactional
    public EspecialidadResponse crear(String nombre, String descripcion) {
        if (especialidadRepository.existsByNombre(nombre)) {
            throw new IllegalArgumentException("Ya existe una especialidad con ese nombre");
        }
        Especialidad esp = Especialidad.builder()
                .nombre(nombre)
                .descripcion(descripcion)
                .activo(true)
                .build();
        esp = especialidadRepository.save(java.util.Objects.requireNonNull(esp));
        return mapToResponse(esp);
    }

    private EspecialidadResponse mapToResponse(Especialidad e) {
        return EspecialidadResponse.builder()
                .idEspecialidad(e.getIdEspecialidad())
                .nombre(e.getNombre())
                .descripcion(e.getDescripcion())
                .activo(e.getActivo())
                .build();
    }
}
