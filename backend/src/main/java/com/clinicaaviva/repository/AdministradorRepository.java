package com.clinicaaviva.repository;

import com.clinicaaviva.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Administrador.
 * Gestiona el acceso a datos de los usuarios administradores del sistema.
 */
@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {

    /**
     * Busca un administrador por su nombre de usuario.
     */
    Optional<Administrador> findByUsuario(String usuario);

    /**
     * Busca un administrador por su correo electronico.
     */
    Optional<Administrador> findByCorreo(String correo);

    /**
     * Verifica si existe un administrador con el usuario dado.
     */
    boolean existsByUsuario(String usuario);

    /**
     * Verifica si existe un administrador con el correo dado.
     */
    boolean existsByCorreo(String correo);
}
