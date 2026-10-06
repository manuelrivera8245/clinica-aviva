package com.clinicaaviva.security;

import com.clinicaaviva.model.enums.RolUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Wrapper de autenticacion que implementa UserDetails de Spring Security.
 * Representa al usuario autenticado en el contexto de seguridad.
 * Soporta tres tipos de usuarios: PACIENTE, MEDICO y ADMINISTRADOR.
 */
@Data
@Builder
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private Integer id;
    private String nombres;
    private String apellidos;
    private String correo;
    private String username;

    @JsonIgnore
    private String password;

    private RolUsuario rol;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Fabrica un UserPrincipal para un paciente.
     */
    public static UserPrincipal ofPaciente(Integer id, String nombres, String apellidos,
                                            String correo, String contrasena) {
        return UserPrincipal.builder()
                .id(id)
                .nombres(nombres)
                .apellidos(apellidos)
                .correo(correo)
                .username(correo)
                .password(contrasena)
                .rol(RolUsuario.PACIENTE)
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_PACIENTE")))
                .build();
    }

    /**
     * Fabrica un UserPrincipal para un medico.
     */
    public static UserPrincipal ofMedico(Integer id, String nombres, String apellidos,
                                          String usuario, String contrasena) {
        return UserPrincipal.builder()
                .id(id)
                .nombres(nombres)
                .apellidos(apellidos)
                .correo(null)
                .username(usuario)
                .password(contrasena)
                .rol(RolUsuario.MEDICO)
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_MEDICO")))
                .build();
    }

    /**
     * Fabrica un UserPrincipal para un administrador.
     */
    public static UserPrincipal ofAdministrador(Integer id, String nombres, String apellidos,
                                                 String usuario, String contrasena) {
        return UserPrincipal.builder()
                .id(id)
                .nombres(nombres)
                .apellidos(apellidos)
                .correo(null)
                .username(usuario)
                .password(contrasena)
                .rol(RolUsuario.ADMINISTRADOR)
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_ADMINISTRADOR")))
                .build();
    }
}
