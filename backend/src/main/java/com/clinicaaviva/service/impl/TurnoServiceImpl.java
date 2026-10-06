package com.clinicaaviva.service.impl;

import com.clinicaaviva.dto.request.TurnoRequest;
import com.clinicaaviva.dto.response.TurnoDisponibleResponse;
import com.clinicaaviva.entity.HorarioMedico;
import com.clinicaaviva.entity.Medico;
import com.clinicaaviva.entity.Turno;
import com.clinicaaviva.model.enums.EstadoTurno;
import com.clinicaaviva.repository.HorarioMedicoRepository;
import com.clinicaaviva.repository.MedicoRepository;
import com.clinicaaviva.repository.TurnoRepository;
import com.clinicaaviva.service.TurnoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TurnoServiceImpl implements TurnoService {

    private final TurnoRepository turnoRepository;
    private final MedicoRepository medicoRepository;
    private final HorarioMedicoRepository horarioMedicoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TurnoDisponibleResponse> listarTurnosDisponibles() {
        return turnoRepository.findTurnosDisponibles(LocalDate.now()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoDisponibleResponse> listarTurnosDisponiblesPorEspecialidad(Integer idEspecialidad) {
        return turnoRepository.findTurnosDisponiblesByEspecialidad(java.util.Objects.requireNonNull(idEspecialidad), LocalDate.now()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TurnoDisponibleResponse> listarTurnosDisponiblesPorMedico(Integer idMedico, LocalDate fecha) {
        if (fecha != null) {
            return turnoRepository.findTurnosLibresByMedicoAndFecha(java.util.Objects.requireNonNull(idMedico), fecha).stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        } else {
            return turnoRepository.findTurnosLibresByMedicoDesdeFecha(java.util.Objects.requireNonNull(idMedico), LocalDate.now()).stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public void generarTurnosDesdeHorario(Integer idMedico, LocalDate fechaDesde, LocalDate fechaHasta) {
        if (!medicoRepository.existsById(java.util.Objects.requireNonNull(idMedico))) {
            throw new IllegalArgumentException("Médico no encontrado: " + idMedico);
        }
        if (fechaHasta.isBefore(fechaDesde)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la de inicio");
        }

        List<HorarioMedico> horarios = horarioMedicoRepository.findByMedicoIdMedicoAndActivoTrue(idMedico);
        if (horarios.isEmpty()) {
            throw new IllegalStateException("El médico no tiene horarios configurados");
        }

        Medico medico = medicoRepository.getReferenceById(java.util.Objects.requireNonNull(idMedico));
        int turnosCreados = 0;
        int turnosDuplicados = 0;

        // Iterar cada día del rango
        LocalDate dia = fechaDesde;
        while (!dia.isAfter(fechaHasta)) {
            final int diaSemana = dia.getDayOfWeek().getValue();

            for (HorarioMedico horario : horarios) {
                if (!horario.getDiaSemana().equals(diaSemana)) continue;

                // Dividir bloque horario en slots
                int duracion = (horario.getDuracionTurnoMin() != null && horario.getDuracionTurnoMin() > 0)
                        ? horario.getDuracionTurnoMin() : 30;

                LocalTime inicio = horario.getHoraInicio();
                while (true) {
                    LocalTime fin = inicio.plusMinutes(duracion);
                    
                    // Detectar cruce de medianoche
                    boolean cruzoMedianoche = fin.isBefore(inicio) || fin.equals(LocalTime.MIDNIGHT);
                    
                    if (cruzoMedianoche) {
                        // Permitir exacto a medianoche
                        if (fin.equals(LocalTime.MIDNIGHT) && horario.getHoraFin().equals(LocalTime.of(23, 59))) {
                            fin = LocalTime.of(23, 59);
                        } else {
                            break;
                        }
                    } else if (fin.isAfter(horario.getHoraFin())) {
                        break;
                    }

                    // Evitar duplicados
                    boolean existe = turnoRepository.existsByMedicoIdMedicoAndFechaAndHoraInicio(
                            java.util.Objects.requireNonNull(idMedico), dia, inicio);
                    if (!existe) {
                        Turno nuevoTurno = Turno.builder()
                                .medico(medico)
                                .fecha(dia)
                                .horaInicio(inicio)
                                .horaFin(fin)
                                .estado(EstadoTurno.Libre)
                                .build();
                        nuevoTurno = turnoRepository.save(java.util.Objects.requireNonNull(nuevoTurno));
                        turnosCreados++;
                    } else {
                        turnosDuplicados++;
                    }
                    
                    if (cruzoMedianoche) {
                        break;
                    }
                    inicio = fin;
                }
            }
            dia = dia.plusDays(1);
        }
        log.info("Generación completada para médico {}: {} turnos creados, {} omitidos por duplicado",
                idMedico, turnosCreados, turnosDuplicados);
    }

    private TurnoDisponibleResponse mapToResponse(Turno t) {
        return TurnoDisponibleResponse.builder()
                .idTurno(t.getIdTurno())
                .fecha(t.getFecha())
                .horaInicio(t.getHoraInicio())
                .horaFin(t.getHoraFin())
                .idMedico(t.getMedico().getIdMedico())
                .nombreMedico(t.getMedico().getNombres() + " " + t.getMedico().getApellidos())
                .idEspecialidad(t.getMedico().getEspecialidad().getIdEspecialidad())
                .especialidad(t.getMedico().getEspecialidad().getNombre())
                .build();
    }

    @Override
    @Transactional
    public TurnoDisponibleResponse crearTurno(TurnoRequest request) {
        // Validar que el medico existe
        if (!medicoRepository.existsById(java.util.Objects.requireNonNull(request.getIdMedico()))) {
            throw new IllegalArgumentException("Medico no encontrado: " + request.getIdMedico());
        }
        Turno turno = Turno.builder()
                .medico(medicoRepository.getReferenceById(java.util.Objects.requireNonNull(request.getIdMedico())))
                .fecha(request.getFecha())
                .horaInicio(request.getHoraInicio())
                .horaFin(request.getHoraFin())
                .estado(EstadoTurno.Libre)
                .build();
        Turno guardado = turnoRepository.save(java.util.Objects.requireNonNull(turno));
        return mapToResponse(guardado);
    }

    @Override
    @Transactional
    public void eliminarTurno(Integer idTurno) {
        Turno turno = turnoRepository.findById(java.util.Objects.requireNonNull(idTurno))
                .orElseThrow(() -> new IllegalArgumentException("Turno no encontrado: " + idTurno));
        // No permitir eliminar turnos con cita activa
        if (turno.getEstado() == EstadoTurno.Ocupado) {
            throw new IllegalArgumentException("No se puede eliminar un turno ocupado (tiene cita asociada)");
        }
        turnoRepository.deleteById(java.util.Objects.requireNonNull(idTurno));
    }
}
