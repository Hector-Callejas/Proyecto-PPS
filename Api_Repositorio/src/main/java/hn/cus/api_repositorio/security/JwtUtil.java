/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.cus.api_repositorio.security;

/**
 *
 * @author EG490082
 */

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "clave-super-secreta-segura-para-jwt-token-generation";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Duración del token (5 horas)
    private final long JWT_EXPIRATION_MS = 1000 * 60 * 60 * 5;

    // Generar token
    public String generarToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(key)
                .compact();
    }

    // Obtener username desde el token
    public String extraerUsername(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    // Obtener fecha de expiración
    public Date extraerExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

    // Validar token
    public boolean validarToken(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        final String username = extraerUsername(token);
        return (username.equals(userDetails.getUsername()) && !estaExpirado(token));
    }

    // Verificar si expiró
    private boolean estaExpirado(String token) {
        return extraerExpiracion(token).before(new Date());
    }

    // Extraer claims genéricos
    public <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerTodosLosClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extraerTodosLosClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
