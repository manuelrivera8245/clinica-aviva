package com.clinicaaviva.service;

import com.clinicaaviva.dto.request.LoginRequest;
import com.clinicaaviva.dto.request.RegistroPacienteRequest;
import com.clinicaaviva.dto.response.JwtAuthenticationResponse;

/**
 * Servicio de autenticacion y registro de usuarios.
 */
public interface AuthService {

    /**
     * Autentica un usuario y retorna un token JWT.
     */
    JwtAuthenticationResponse login(LoginRequest request);

    /**
     * Registra un nuevo paciente en el sistema.
     */
    JwtAuthenticationResponse registrarPaciente(RegistroPacienteRequest request);

    /**
     * Verifica si una credencial (correo, DNI o usuario) ya esta registrada.
     */
    boolean existeCredencial(String credencial);
}
