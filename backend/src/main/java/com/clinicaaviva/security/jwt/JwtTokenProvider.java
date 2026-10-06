package com.clinicaaviva.security.jwt;

import com.clinicaaviva.security.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Proveedor de tokens JWT.
 * Genera, valida y extrae informacion de los tokens JWT utilizados
 * para la autenticacion stateless entre el frontend Angular y el backend.
 */
@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey jwtSecret;
    private final long jwtExpirationMs;

    public JwtTokenProvider(
            @Value("${jwt.secret:clinicaAvivaSecretKey2026JWTTokenSeguro}") String jwtSecretString,
            @Value("${jwt.expiration:86400000}") long jwtExpirationMs) {
        this.jwtSecret = Keys.hmacShaKeyFor(jwtSecretString.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationMs = jwtExpirationMs;
    }

    /**
     * Genera un token JWT a partir de la autenticacion exitosa.
     */
    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationMs);

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .claim("id", userPrincipal.getId())
                .claim("rol", userPrincipal.getRol().name())
                .claim("nombres", userPrincipal.getNombres())
                .claim("apellidos", userPrincipal.getApellidos())
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(jwtSecret)
                .compact();
    }

    /**
     * Extrae el nombre de usuario (subject) del token.
     */
    public String getUsernameFromToken(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    /**
     * Extrae el rol del usuario del token.
     */
    public String getRolFromToken(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("rol", String.class);
    }

    /**
     * Extrae el ID del usuario del token.
     */
    public Integer getIdFromToken(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("id", Integer.class);
    }

    /**
     * Valida la firma y expiracion del token.
     */
    public boolean validateToken(String token) {

        try {

            Jwts.parser()
                    .verifyWith(jwtSecret)
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (SecurityException ex) {
            log.error("Firma JWT invalida: {}", ex.getMessage());

        } catch (MalformedJwtException ex) {
            log.error("Token JWT malformado: {}", ex.getMessage());

        } catch (ExpiredJwtException ex) {
            log.error("Token JWT expirado: {}", ex.getMessage());

        } catch (UnsupportedJwtException ex) {
            log.error("Token JWT no soportado: {}", ex.getMessage());

        } catch (IllegalArgumentException ex) {
            log.error("Claims JWT vacio: {}", ex.getMessage());
        }

        return false;
    }
}
