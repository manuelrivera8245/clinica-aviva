package com.clinicaaviva.repository;

import com.clinicaaviva.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Especialidad.
 * Proporciona operaciones CRUD y consultas personalizadas para gestionar
 * las especialidades medicas disponibles en la clinica.
 */
@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {

    /**
     * Busca especialidades cuyo nombre contenga el termino (ignora mayusculas/minusculas).
     */
    List<Especialidad> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca una especialidad por su nombre exacto.
     */
    Optional<Especialidad> findByNombre(String nombre);

    /**
     * Lista todas las especialidades activas (borrado logico).
     */
    List<Especialidad> findByActivoTrue();

    /**
     * Verifica si existe una especialidad con el nombre dado.
     */
    boolean existsByNombre(String nombre);
}
