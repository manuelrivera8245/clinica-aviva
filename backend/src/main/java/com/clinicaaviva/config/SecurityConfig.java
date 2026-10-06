package com.clinicaaviva.config;

import com.clinicaaviva.security.UserDetailsServiceImpl;
import com.clinicaaviva.security.jwt.JwtAuthenticationEntryPoint;
import com.clinicaaviva.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuracion principal de Spring Security.
 * Define la cadena de filtros, reglas de acceso por rol,
 * autenticacion stateless con JWT y encriptacion BCrypt.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Codificador de contrasenas BCrypt (strength=10 por defecto).
     * Todas las contrasenas se almacenan hasheadas en la base de datos.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Cadena de filtros de seguridad con reglas de acceso por rol.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                // === ENDPOINTS PUBLICOS ===
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/configuracion").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/especialidades").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/especialidades/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/turnos/disponibles").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/turnos/disponibles/**").permitAll()
                // B-06: GET /api/medicos/especialidad/{id} es publico — lo necesitan los pacientes
                // en el flujo de reserva de citas (RF-03) para filtrar medicos por especialidad.
                // Debe ir ANTES de la regla .requestMatchers("/api/medicos/**").hasRole(ADMINISTRADOR)
                // porque Spring Security evalua en orden y la primera regla que coincide gana.
                .requestMatchers(HttpMethod.GET, "/api/medicos/especialidad/**").permitAll()

                // === ROL PACIENTE ===
                // Nota: /api/pacientes/perfil y /api/citas/* son exclusivos de PACIENTE.
                // GET /api/pacientes (listar) y DELETE /api/pacientes/{id} son de ADMINISTRADOR
                // y se declaran en la seccion ADMINISTRADOR (mas abajo) — deben ir antes
                // de esta regla generica para que no sean capturados por PACIENTE.
                .requestMatchers("/api/pacientes/perfil").hasRole("PACIENTE")
                .requestMatchers("/api/citas/mis-citas").hasRole("PACIENTE")
                .requestMatchers("/api/citas/historial").hasRole("PACIENTE")
                .requestMatchers("/api/turnos/disponibles").hasRole("PACIENTE")
                .requestMatchers("/api/citas/reservar").hasRole("PACIENTE")
                .requestMatchers("/api/citas/cancelar").hasRole("PACIENTE")
                .requestMatchers("/api/notificaciones/**").hasRole("PACIENTE")

                // === ROL MEDICO ===
                .requestMatchers("/api/medicos/perfil").hasRole("MEDICO")
                .requestMatchers("/api/citas/mis-citas-medico").hasRole("MEDICO")
                .requestMatchers("/api/citas/actualizar-estado").hasRole("MEDICO")
                .requestMatchers("/api/citas/agenda-dia").hasRole("MEDICO")
                // BUG FIX D: el medico usaba /api/citas/historial (PACIENTE) y recibia 401.
                // Se agrega regla para /api/citas/historial-medico (endpoint propio del MEDICO).
                .requestMatchers("/api/citas/historial-medico").hasRole("MEDICO")
                .requestMatchers("/api/dashboard/medico").hasRole("MEDICO")

                // === ROL ADMINISTRADOR ===
                .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/medicos/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/especialidades/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/horarios/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/turnos/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/dashboard/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/notificaciones/**").hasRole("ADMINISTRADOR")
                // GET /api/pacientes y DELETE /api/pacientes/{id} (admin)
                // Se declaran ANTES de la regla generica PACIENTE para que tengan precedencia.
                .requestMatchers(HttpMethod.GET, "/api/pacientes").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/pacientes/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/reportes/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/api/configuracion").hasRole("ADMINISTRADOR")

                // Cualquier otra ruta requiere autenticacion
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
