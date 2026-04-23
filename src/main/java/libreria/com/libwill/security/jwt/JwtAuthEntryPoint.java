package libreria.com.libwill.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.io.IOException;


/**
 * Manejador para respuestas 401 (no autorizado)
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {


    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);


    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {


        String message = (authException != null && authException.getMessage() != null)
                ? authException.getMessage()
                : "Token inválido o no proporcionado";


        logger.error("Error de autenticación no autorizada: {}", message);


        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("""
           {
             "path": "%s",
             "error": "Unauthorized",
             "message": "Error de autenticación: %s",
             "status": 401
           }
           """.formatted(request.getRequestURI(), message));
    }
}
