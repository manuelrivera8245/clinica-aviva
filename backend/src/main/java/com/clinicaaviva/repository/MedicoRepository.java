package com.clinicaaviva.repository;

import com.clinicaaviva.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Medico.
 * Proporciona operaciones para gestionar el personal medico,
 * filtrar por especialidad y autenticacion.
 */
@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {

    /**
     * Busca un medico por su nombre de usuario.
     */
    Optional<Medico> findByUsuario(String usuario);

    /**
     * Busca un medico por su correo institucional.
     */
    Optional<Medico> findByCorreo(String correo);

    /**
     * Lista todos los medicos activos de una especialidad especifica.
     */
    List<Medico> findByEspecialidadIdEspecialidadAndActivoTrue(Integer idEspecialidad);

    /**
     * Lista todos los medicos activos con sus especialidades.
     */
    @Query("SELECT m FROM Medico m JOIN FETCH m.especialidad WHERE m.activo = true")
    List<Medico> findAllActivosWithEspecialidad();

    /**
     * Busca medicos por nombre o apellido (busqueda parcial).
     */
    @Query("SELECT m FROM Medico m WHERE LOWER(CONCAT(m.nombres, ' ', m.apellidos)) LIKE LOWER(CONCAT('%', :nombre, '%')) AND m.activo = true")
    List<Medico> findByNombreCompletoContaining(@Param("nombre") String nombre);

    /**
     * Verifica si existe un medico con el usuario dado.
     */
    boolean existsByUsuario(String usuario);

    /**
     * Verifica si existe un medico con el correo dado.
     */
    boolean existsByCorreo(String correo);
}
