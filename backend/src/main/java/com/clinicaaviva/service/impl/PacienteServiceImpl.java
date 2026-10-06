package com.clinicaaviva.service.impl;

import com.clinicaaviva.dto.request.ActualizarPerfilPacienteRequest;
import com.clinicaaviva.dto.response.PacienteResponse;
import com.clinicaaviva.entity.Paciente;
import com.clinicaaviva.repository.PacienteRepository;
import com.clinicaaviva.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse obtenerPerfil(Integer idPaciente) {
        Paciente paciente = pacienteRepository.findById(java.util.Objects.requireNonNull(idPaciente))
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
        return mapToResponse(paciente);
    }

    /**
     * PUT /api/pacientes/perfil
     * Actualiza los datos editables del paciente autenticado.
     * Solo modifica los campos no nulos del request (actualizacion parcial).
     * BUG FIX: el endpoint no existia — el frontend recibia 404 al intentar guardar.
     */
    @Override
    @Transactional
    public PacienteResponse actualizarPerfil(Integer idPaciente, ActualizarPerfilPacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(java.util.Objects.requireNonNull(idPaciente))
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        if (request.getNombres() != null && !request.getNombres().isBlank()) {
            paciente.setNombres(request.getNombres());
        }
        if (request.getApellidos() != null && !request.getApellidos().isBlank()) {
            paciente.setApellidos(request.getApellidos());
        }
        if (request.getCorreo() != null && !request.getCorreo().isBlank()) {
            paciente.setCorreo(request.getCorreo());
        }
        if (request.getTelefono() != null && !request.getTelefono().isBlank()) {
            paciente.setTelefono(request.getTelefono());
        }
        if (request.getContrasena() != null && !request.getContrasena().isBlank()) {
            paciente.setContrasena(passwordEncoder.encode(request.getContrasena()));
        }

        paciente = pacienteRepository.save(java.util.Objects.requireNonNull(paciente));
        return mapToResponse(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> listarTodos() {
        return pacienteRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public long contarPacientes() {
        return pacienteRepository.count();
    }

    @Override
    @Transactional
    public void desactivarPaciente(Integer idPaciente) {
        Paciente paciente = pacienteRepository.findById(java.util.Objects.requireNonNull(idPaciente))
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado: " + idPaciente));
        paciente.setActivo(false);
        paciente = pacienteRepository.save(java.util.Objects.requireNonNull(paciente));
    }

    private PacienteResponse mapToResponse(Paciente p) {
        return PacienteResponse.builder()
                .idPaciente(p.getIdPaciente())
                .dni(p.getDni())
                .nombres(p.getNombres())
                .apellidos(p.getApellidos())
                .correo(p.getCorreo())
                .telefono(p.getTelefono())
                .fechaRegistro(p.getFechaRegistro())
                .activo(p.getActivo())
                .build();
    }
}
