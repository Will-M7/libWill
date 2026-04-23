package libreria.com.libwill.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;


/**
 * DTO para la respuesta después de un login exitoso
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDTO {


    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String nombre;
    private String apellido;
    private String direccion;
    private String numero;
    private List<String> roles;


    // Constructor personalizado sin el tipo
    public JwtResponseDTO(String token, Long id, String username, String email,
                          String nombre, String apellido,String direccion,
                          String numero, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.numero = numero;
        this.direccion = direccion;
        this.roles = roles;

    }
}
