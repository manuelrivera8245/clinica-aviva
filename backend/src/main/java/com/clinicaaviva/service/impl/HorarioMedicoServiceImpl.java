package com.clinicaaviva.service.impl;

import com.clinicaaviva.dto.request.HorarioMedicoRequest;
import com.clinicaaviva.dto.response.HorarioMedicoResponse;
import com.clinicaaviva.entity.HorarioMedico;
import com.clinicaaviva.repository.AdministradorRepository;
import com.clinicaaviva.repository.HorarioMedicoRepository;
import com.clinicaaviva.repository.MedicoRepository;
import com.clinicaaviva.service.HorarioMedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de horarios médicos (RF-11).
 */
@Service
@RequiredArgsConstructor
public class HorarioMedicoServiceImpl implements HorarioMedicoService {

    private final HorarioMedicoRepository horarioRepository;
    private final MedicoRepository        medicoRepository;
    private final AdministradorRepository administradorRepository;

    private static final String[] DIAS = {"", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

    @Override
    @Transactional(readOnly = true)
    public List<HorarioMedicoResponse> listarPorMedico(Integer idMedico) {
        return horarioRepository.findByMedicoIdMedicoAndActivoTrue(idMedico)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public HorarioMedicoResponse crear(HorarioMedicoRequest request, Integer idAdministrador) {
        if (!medicoRepository.existsById(java.util.Objects.requireNonNull(request.getIdMedico()))) {
            throw new IllegalArgumentException("Médico no encontrado: " + request.getIdMedico());
        }
        if (request.getHoraFin().compareTo(request.getHoraInicio()) <= 0) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }

        HorarioMedico horario = HorarioMedico.builder()
                .medico(medicoRepository.getReferenceById(java.util.Objects.requireNonNull(request.getIdMedico())))
                .creadoPor(administradorRepository.getReferenceById(java.util.Objects.requireNonNull(idAdministrador)))
                .diaSemana(request.getDiaSemana())
                .horaInicio(request.getHoraInicio())
                .horaFin(request.getHoraFin())
                .duracionTurnoMin(request.getDuracionTurnoMin() != null ? request.getDuracionTurnoMin() : 30)
                .activo(true)
                .build();

        return mapToResponse(horarioRepository.save(java.util.Objects.requireNonNull(horario)));
    }

    @Override
    @Transactional
    public void eliminar(Integer idHorario) {
        HorarioMedico horario = horarioRepository.findById(java.util.Objects.requireNonNull(idHorario))
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado: " + idHorario));
        horario.setActivo(false);
        horario = horarioRepository.save(java.util.Objects.requireNonNull(horario));
    }

    private HorarioMedicoResponse mapToResponse(HorarioMedico h) {
        String nombreDia = (h.getDiaSemana() >= 1 && h.getDiaSemana() <= 7)
                ? DIAS[h.getDiaSemana()] : "Desconocido";
        return HorarioMedicoResponse.builder()
                .idHorario(h.getIdHorario())
                .idMedico(h.getMedico().getIdMedico())
                .nombreMedico(h.getMedico().getNombres() + " " + h.getMedico().getApellidos())
                .diaSemana(h.getDiaSemana())
                .diaNombre(nombreDia)
                .horaInicio(h.getHoraInicio())
                .horaFin(h.getHoraFin())
                .duracionTurnoMin(h.getDuracionTurnoMin())
                .activo(h.getActivo())
                .build();
    }
}
