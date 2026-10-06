package com.clinicaaviva.service.impl;

import com.clinicaaviva.dto.request.LoginRequest;
import com.clinicaaviva.dto.request.RegistroPacienteRequest;
import com.clinicaaviva.dto.response.JwtAuthenticationResponse;
import com.clinicaaviva.entity.Paciente;
import com.clinicaaviva.model.enums.RolUsuario;
import com.clinicaaviva.repository.AdministradorRepository;
import com.clinicaaviva.repository.MedicoRepository;
import com.clinicaaviva.repository.PacienteRepository;
import com.clinicaaviva.security.UserPrincipal;
import com.clinicaaviva.security.jwt.JwtTokenProvider;
import com.clinicaaviva.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Servicio de autenticacion
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final AdministradorRepository administradorRepository;

    @Override
    public JwtAuthenticationResponse login(LoginRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getCredencial(),
                            request.getContrasena()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

            return JwtAuthenticationResponse.builder()
                    .token(token)
                    .tipo("Bearer")
                    .idUsuario(principal.getId())
                    .nombres(principal.getNombres())
                    .apellidos(principal.getApellidos())
                    .correo(principal.getCorreo())
                    .rol(principal.getRol())
                    .build();

        } catch (BadCredentialsException e) {
            log.warn("Intento de login fallido para: {}", request.getCredencial());
            throw new BadCredentialsException("Credenciales invalidas. Verifique su usuario y contrasena.");
        }
    }

    @Override
    @Transactional
    public JwtAuthenticationResponse registrarPaciente(RegistroPacienteRequest request) {
        // Validar que no exista el DNI
        if (pacienteRepository.existsByDni(request.getDni())) {
            throw new IllegalArgumentException("Ya existe un paciente registrado con el DNI: " + request.getDni());
        }

        // Validar que no exista el correo
        if (pacienteRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException(
                    "Ya existe un paciente registrado con el correo: " + request.getCorreo());
        }

        // Crear paciente
        Paciente paciente = Paciente.builder()
                .dni(request.getDni())
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .correo(request.getCorreo())
                .telefono(request.getTelefono())
                .contrasena(passwordEncoder.encode(request.getContrasena()))
                .activo(true)
                .build();

        paciente = pacienteRepository.save(java.util.Objects.requireNonNull(paciente));
        log.info("Paciente registrado exitosamente: {} {}", request.getNombres(), request.getApellidos());

        // Autenticar automaticamente al nuevo paciente
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        paciente.getCorreo(),
                        request.getContrasena()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        return JwtAuthenticationResponse.builder()
                .token(token)
                .tipo("Bearer")
                .idUsuario(paciente.getIdPaciente())
                .nombres(paciente.getNombres())
                .apellidos(paciente.getApellidos())
                .correo(paciente.getCorreo())
                .rol(RolUsuario.PACIENTE)
                .build();
    }

    @Override
    public boolean existeCredencial(String credencial) {
        return pacienteRepository.existsByDni(credencial)
                || pacienteRepository.existsByCorreo(credencial)
                || medicoRepository.existsByUsuario(credencial)
                || medicoRepository.existsByCorreo(credencial)
                || administradorRepository.existsByUsuario(credencial)
                || administradorRepository.existsByCorreo(credencial);
    }
}
