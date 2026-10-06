package com.clinicaaviva.security;

import com.clinicaaviva.entity.Administrador;
import com.clinicaaviva.entity.Medico;
import com.clinicaaviva.entity.Paciente;
import com.clinicaaviva.repository.AdministradorRepository;
import com.clinicaaviva.repository.MedicoRepository;
import com.clinicaaviva.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Servicio de detalles de usuario
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final AdministradorRepository administradorRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. Buscar como PACIENTE (por correo o DNI)
        Paciente paciente = pacienteRepository.findByCorreoOrDni(username).orElse(null);
        if (paciente != null && Boolean.TRUE.equals(paciente.getActivo())) {
            log.debug("Usuario autenticado como PACIENTE: {}", username);
            return UserPrincipal.ofPaciente(
                    paciente.getIdPaciente(),
                    paciente.getNombres(),
                    paciente.getApellidos(),
                    paciente.getCorreo(),
                    paciente.getContrasena()
            );
        }

        // 2. Buscar como MEDICO
        Medico medico = medicoRepository.findByUsuario(username)
                .or(() -> medicoRepository.findByCorreo(username))
                .orElse(null);
        if (medico != null && Boolean.TRUE.equals(medico.getActivo())) {
            log.debug("Usuario autenticado como MEDICO: {}", username);
            // El JWT se firma con el campo usuario
            return UserPrincipal.ofMedico(
                    medico.getIdMedico(),
                    medico.getNombres(),
                    medico.getApellidos(),
                    medico.getUsuario(),
                    medico.getContrasena()
            );
        }

        // 3. Buscar como ADMINISTRADOR (por usuario)
        Administrador admin = administradorRepository.findByUsuario(username).orElse(null);
        if (admin != null && Boolean.TRUE.equals(admin.getActivo())) {
            log.debug("Usuario autenticado como ADMINISTRADOR: {}", username);
            return UserPrincipal.ofAdministrador(
                    admin.getIdAdministrador(),
                    admin.getNombres(),
                    admin.getApellidos(),
                    admin.getUsuario(),
                    admin.getContrasena()
            );
        }

        log.warn("Usuario no encontrado o inactivo: {}", username);
        throw new UsernameNotFoundException(
                "Usuario no encontrado o credenciales invalidas: " + username);
    }
}
