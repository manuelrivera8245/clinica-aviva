package com.clinicaaviva.service.impl;

import com.clinicaaviva.dto.response.NotificacionResponse;
import com.clinicaaviva.entity.Notificacion;
import com.clinicaaviva.model.enums.EstadoCita;
import com.clinicaaviva.model.enums.TipoNotificacion;
import com.clinicaaviva.repository.NotificacionRepository;
import com.clinicaaviva.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponse> obtenerMisNotificaciones(Integer idPaciente) {
        List<Notificacion> notificaciones = notificacionRepository
                .findMisNotificaciones(
                        idPaciente, 
                        LocalDateTime.now(),
                        TipoNotificacion.Recordatorio,
                        EstadoCita.Programada
                );

        return notificaciones.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void marcarComoLeidas(Integer idPaciente) {
        notificacionRepository.marcarComoLeidasPorPaciente(idPaciente);
    }

    private NotificacionResponse mapToResponse(Notificacion notificacion) {
        String medicoNombres = notificacion.getCita().getTurno().getMedico().getNombres() + " " +
                notificacion.getCita().getTurno().getMedico().getApellidos();
        String especialidad = notificacion.getCita().getTurno().getMedico().getEspecialidad().getNombre();
        String fechaCita = notificacion.getCita().getTurno().getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String horaCita = notificacion.getCita().getTurno().getHoraInicio().toString();

        String mensaje = "";
        switch (notificacion.getTipo()) {
            case Confirmacion:
                mensaje = "Cita confirmada para el " + fechaCita + " a las " + horaCita + " en " + especialidad + " con el Dr(a). " + medicoNombres + ".";
                break;
            case Recordatorio:
                mensaje = "Recuerde su cita mañana " + fechaCita + " a las " + horaCita + " en " + especialidad + " con el Dr(a). " + medicoNombres + ".";
                break;
            case Cancelacion:
                mensaje = "Su cita para el " + fechaCita + " a las " + horaCita + " ha sido cancelada.";
                break;
        }

        return NotificacionResponse.builder()
                .idNotificacion(notificacion.getIdNotificacion())
                .tipo(notificacion.getTipo().name())
                .mensaje(mensaje)
                .fecha(notificacion.getFechaProgramada())
                .leido(notificacion.getLeido())
                .build();
    }
}
