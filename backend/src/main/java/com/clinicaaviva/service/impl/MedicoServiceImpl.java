package com.clinicaaviva.service.impl;

import com.clinicaaviva.dto.request.MedicoRequest;
import com.clinicaaviva.dto.response.EspecialidadResponse;
import com.clinicaaviva.dto.response.MedicoResponse;
import com.clinicaaviva.entity.Administrador;
import com.clinicaaviva.entity.Especialidad;
import com.clinicaaviva.entity.Medico;
import com.clinicaaviva.repository.AdministradorRepository;
import com.clinicaaviva.repository.EspecialidadRepository;
import com.clinicaaviva.repository.MedicoRepository;
import com.clinicaaviva.service.MedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicoServiceImpl implements MedicoService {

    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public MedicoResponse crearMedico(MedicoRequest request, Integer idAdmin) {
        // Contraseña es obligatoria al crear (no al actualizar)
        if (request.getContrasena() == null || request.getContrasena().isBlank()) {
            throw new IllegalArgumentException("La contrasena es obligatoria al crear un medico");
        }

        Especialidad especialidad = especialidadRepository.findById(java.util.Objects.requireNonNull(request.getIdEspecialidad()))
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));

        Administrador admin = administradorRepository.findById(java.util.Objects.requireNonNull(idAdmin))
                .orElseThrow(() -> new IllegalArgumentException("Administrador no encontrado"));

        Medico medico = Medico.builder()
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .correo(request.getCorreo())
                .usuario(request.getUsuario())
                .contrasena(passwordEncoder.encode(request.getContrasena()))
                .especialidad(especialidad)
                .creadoPor(admin)
                .activo(true)
                .build();

        medico = medicoRepository.save(java.util.Objects.requireNonNull(medico));
        return mapToResponse(medico);
    }

    @Override
    @Transactional
    public MedicoResponse actualizarMedico(Integer idMedico, MedicoRequest request) {
        Medico medico = medicoRepository.findById(java.util.Objects.requireNonNull(idMedico))
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));

        Especialidad especialidad = especialidadRepository.findById(java.util.Objects.requireNonNull(request.getIdEspecialidad()))
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));

        medico.setNombres(request.getNombres());
        medico.setApellidos(request.getApellidos());
        medico.setCorreo(request.getCorreo());
        medico.setEspecialidad(especialidad);
        if (request.getContrasena() != null && !request.getContrasena().isEmpty()) {
            medico.setContrasena(passwordEncoder.encode(request.getContrasena()));
        }

        medico = medicoRepository.save(java.util.Objects.requireNonNull(medico));
        return mapToResponse(medico);
    }

    @Override
    @Transactional
    public void darDeBajaMedico(Integer idMedico) {
        Medico medico = medicoRepository.findById(java.util.Objects.requireNonNull(idMedico))
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));
        medico.setActivo(false);
        medico = medicoRepository.save(java.util.Objects.requireNonNull(medico));
    }

    @Override
    @Transactional(readOnly = true)
    public MedicoResponse obtenerPorId(Integer idMedico) {
        Medico medico = medicoRepository.findById(java.util.Objects.requireNonNull(idMedico))
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));
        return mapToResponse(medico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicoResponse> listarTodos() {
        return medicoRepository.findAllActivosWithEspecialidad().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicoResponse> listarPorEspecialidad(Integer idEspecialidad) {
        return medicoRepository.findByEspecialidadIdEspecialidadAndActivoTrue(java.util.Objects.requireNonNull(idEspecialidad)).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public long contarMedicosActivos() {
        return medicoRepository.findAllActivosWithEspecialidad().size();
    }

    private MedicoResponse mapToResponse(Medico m) {
        return MedicoResponse.builder()
                .idMedico(m.getIdMedico())
                .nombres(m.getNombres())
                .apellidos(m.getApellidos())
                .correo(m.getCorreo())
                .usuario(m.getUsuario())
                .activo(m.getActivo())
                .especialidad(EspecialidadResponse.builder()
                        .idEspecialidad(m.getEspecialidad().getIdEspecialidad())
                        .nombre(m.getEspecialidad().getNombre())
                        .descripcion(m.getEspecialidad().getDescripcion())
                        .activo(m.getEspecialidad().getActivo())
                        .build())
                .build();
    }
}
