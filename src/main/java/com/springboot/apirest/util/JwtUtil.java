package com.springboot.apirest.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "secreto_super_seguro_de_32_bytes!!";
    private static final long EXPIRATION_TIME = 86400000; // 24 horas

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    /**
     * Genera un token JWT a partir del email del usuario.
     */
    public String generarToken(String email) {
        return Jwts.builder()
                .subject(email) // Antes: setSubject()
                .issuedAt(new Date()) // Antes: setIssuedAt()
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Antes: setExpiration()
                .signWith(key) // ✅ Se firma solo con la clave, sin especificar el algoritmo
                .compact();
    }

    /**
     * Valida si el token es correcto.
     */
    public boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key) // ✅ Se usa verifyWith() en lugar de setSigningKey()
                    .build()
                    .parse(token); // ✅ Se usa parse() en lugar de parseClaimsJws()
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Obtiene el email desde el token.
     */
    public String obtenerEmailDesdeToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}

