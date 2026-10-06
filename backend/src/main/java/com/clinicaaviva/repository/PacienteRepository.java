package com.clinicaaviva.repository;

import com.clinicaaviva.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Paciente.
 * Gestiona el acceso a datos de los pacientes registrados, incluyendo
 * autenticacion por correo/DNI y consultas de perfil.
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    /**
     * Busca un paciente por su DNI (documento de identidad).
     */
    Optional<Paciente> findByDni(String dni);

    /**
     * Busca un paciente por su correo electronico.
     * Utilizado para el proceso de login.
     */
    Optional<Paciente> findByCorreo(String correo);

    /**
     * Busca un paciente por correo o DNI (login dual).
     */
    @Query("SELECT p FROM Paciente p WHERE p.correo = :credencial OR p.dni = :credencial")
    Optional<Paciente> findByCorreoOrDni(@Param("credencial") String credencial);

    /**
     * Verifica si existe un paciente con el DNI dado.
     */
    boolean existsByDni(String dni);

    /**
     * Verifica si existe un paciente con el correo dado.
     */
    boolean existsByCorreo(String correo);
}
