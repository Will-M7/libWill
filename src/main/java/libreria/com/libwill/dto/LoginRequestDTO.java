package libreria.com.libwill.dto;

import lombok.Data;


/**
 * DTO para las solicitudes de inicio de sesión
 * Se utiliza en /api/auth/login
 */
@Data
public class LoginRequestDTO {
    private String username;
    private String password;
}
