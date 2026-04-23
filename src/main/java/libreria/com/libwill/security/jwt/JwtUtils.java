package libreria.com.libwill.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import libreria.com.libwill.entity.UsuarioEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Utilidades para generar y validar tokens JWT
 */
@Component
public class JwtUtils {


    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);


    @Value("${libwill.app.jwtSecret:SecretKeyLIBWILL1234567890SUPERSEGURA2026!}")
    private String jwtSecret;


    @Value("${libwill.app.jwtExpirationMs:86400000}") // 1 dia
    private int jwtExpirationMs;


    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            keyBytes = java.util.Arrays.copyOf(keyBytes, 32); // asegura longitud >= 256 bits
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }


    /** Genera el token JWT con roles incluidos */
    public String generateJwtToken(Authentication authentication) {
        UsuarioEntity userPrincipal = (UsuarioEntity) authentication.getPrincipal();


        List<String> roles = userPrincipal.getRoles().stream()
                .map(rol -> rol.getNombre().toUpperCase())
                .collect(Collectors.toList());


        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    /** Obtiene el username del token */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    /** Obtiene los roles del token */
    public List<String> getRolesFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();


        Object roles = claims.get("roles");
        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream()
                    .map(Object::toString)
                    .toList();
        }
        return List.of();
    }


    /** Valida el token */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException e) {
            logger.error("Token JWT inválido: {}", e.getMessage());
        }
        return false;
    }
}
