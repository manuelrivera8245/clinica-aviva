package com.clinicaaviva.repository;

import com.clinicaaviva.entity.ConfiguracionGeneral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad ConfiguracionGeneral.
 * Gestiona la informacion dinamica de la clinica.
 * La tabla contiene un unico registro que es accesible publicamente
 * y editable solo por el rol ADMINISTRADOR.
 */
@Repository
public interface ConfiguracionGeneralRepository extends JpaRepository<ConfiguracionGeneral, Integer> {

    /**
     * Obtiene el registro de configuracion activo (unico registro).
     * Este metodo es utilizado por el endpoint publico GET /api/configuracion
     * para mostrar la informacion de la clinica en el landing page.
     */
    @Query("SELECT c FROM ConfiguracionGeneral c WHERE c.id = 1")
    Optional<ConfiguracionGeneral> findConfiguracionActiva();

    /**
     * Verifica si existe algun registro de configuracion.
     */
    @Query("SELECT COUNT(c) > 0 FROM ConfiguracionGeneral c")
    boolean existsConfiguracion();
}
