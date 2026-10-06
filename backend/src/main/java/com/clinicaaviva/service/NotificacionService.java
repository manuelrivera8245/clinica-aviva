package com.clinicaaviva.service;

import com.clinicaaviva.dto.response.NotificacionResponse;
import java.util.List;

public interface NotificacionService {
    List<NotificacionResponse> obtenerMisNotificaciones(Integer idPaciente);
    void marcarComoLeidas(Integer idPaciente);
}
