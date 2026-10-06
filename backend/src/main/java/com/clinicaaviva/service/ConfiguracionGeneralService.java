package com.clinicaaviva.service;

import com.clinicaaviva.dto.request.ConfiguracionGeneralRequest;
import com.clinicaaviva.dto.response.ConfiguracionGeneralResponse;

/**
 * Servicio de gestion de la configuracion general de la clinica.
 * El GET es publico (muestra info en el landing).
 * El PUT solo es accesible por ADMINISTRADOR.
 */
public interface ConfiguracionGeneralService {

    ConfiguracionGeneralResponse obtenerConfiguracion();

    ConfiguracionGeneralResponse actualizarConfiguracion(ConfiguracionGeneralRequest request);
}
