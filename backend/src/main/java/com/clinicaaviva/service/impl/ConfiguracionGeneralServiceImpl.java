package com.clinicaaviva.service.impl;

import com.clinicaaviva.dto.request.ConfiguracionGeneralRequest;
import com.clinicaaviva.dto.response.ConfiguracionGeneralResponse;
import com.clinicaaviva.entity.ConfiguracionGeneral;
import com.clinicaaviva.repository.ConfiguracionGeneralRepository;
import com.clinicaaviva.service.ConfiguracionGeneralService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implementacion del servicio de configuracion general.
 * GET es publico (se muestra en el landing page).
 * PUT solo es accesible por ADMINISTRADOR.
 */
@Service
@RequiredArgsConstructor
public class ConfiguracionGeneralServiceImpl implements ConfiguracionGeneralService {

    private final ConfiguracionGeneralRepository configuracionRepository;

    @Override
    @Transactional(readOnly = true)
    public ConfiguracionGeneralResponse obtenerConfiguracion() {
        ConfiguracionGeneral config = configuracionRepository.findConfiguracionActiva()
                .orElseGet(this::crearDefault);
        return mapToResponse(config);
    }

    @Override
    @Transactional
    public ConfiguracionGeneralResponse actualizarConfiguracion(ConfiguracionGeneralRequest request) {
        ConfiguracionGeneral config = configuracionRepository.findConfiguracionActiva()
                .orElseGet(this::crearDefault);

        config.setNombreClinica(request.getNombreClinica());
        config.setCorreoContacto(request.getCorreoContacto());
        config.setTelefonoContacto(request.getTelefonoContacto());
        config.setDireccion(request.getDireccion());
        config.setHorarioAperturaGeneral(request.getHorarioAperturaGeneral());
        config.setHorarioCierreGeneral(request.getHorarioCierreGeneral());
        config.setEstadoDisponibilidad(request.getEstadoDisponibilidad());
        config.setFechaActualizacion(LocalDateTime.now());

        configuracionRepository.save(config);
        return mapToResponse(config);
    }

    private ConfiguracionGeneral crearDefault() {
        ConfiguracionGeneral config = ConfiguracionGeneral.builder()
                .nombreClinica("Clinica Aviva")
                .correoContacto("contacto@clinicaaviva.pe")
                .telefonoContacto("(01) 712-3456")
                .direccion("Av. Carlos Izaguirre 1200, Los Olivos, Lima, Peru")
                .horarioAperturaGeneral(java.time.LocalTime.of(8, 0))
                .horarioCierreGeneral(java.time.LocalTime.of(20, 0))
                .estadoDisponibilidad(true)
                .build();
        return configuracionRepository.save(java.util.Objects.requireNonNull(config));
    }

    private ConfiguracionGeneralResponse mapToResponse(ConfiguracionGeneral c) {
        return ConfiguracionGeneralResponse.builder()
                .id(c.getId())
                .nombreClinica(c.getNombreClinica())
                .correoContacto(c.getCorreoContacto())
                .telefonoContacto(c.getTelefonoContacto())
                .direccion(c.getDireccion())
                .horarioAperturaGeneral(c.getHorarioAperturaGeneral())
                .horarioCierreGeneral(c.getHorarioCierreGeneral())
                .estadoDisponibilidad(c.getEstadoDisponibilidad())
                .build();
    }
}
