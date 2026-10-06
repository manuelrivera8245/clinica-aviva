package com.clinicaaviva.repository;

import com.clinicaaviva.entity.HorarioMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad HorarioMedico.
 * Gestiona los horarios de atencion configurados para cada medico.
 */
@Repository
public interface HorarioMedicoRepository extends JpaRepository<HorarioMedico, Integer> {

    /**
     * Lista los horarios activos de un medico especifico.
     */
    List<HorarioMedico> findByMedicoIdMedicoAndActivoTrue(Integer idMedico);

    /**
     * Lista los horarios de un medico filtrados por dia de la semana.
     */
    List<HorarioMedico> findByMedicoIdMedicoAndDiaSemanaAndActivoTrue(Integer idMedico, Integer diaSemana);

    /**
     * Lista todos los horarios activos con informacion del medico y especialidad.
     */
    List<HorarioMedico> findByActivoTrue();

    /**
     * Elimina (logicamente) todos los horarios de un medico.
     */
    void deleteByMedicoIdMedico(Integer idMedico);
}
