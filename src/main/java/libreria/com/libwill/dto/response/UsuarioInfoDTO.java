package libreria.com.libwill.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UsuarioInfoDTO {
    private Long id;
    private String username;
    private String email;
    private String nombre;
    private String apellido;
    private String direccion;
    private String numero;
    private Boolean activo;
    private List<String> roles;
}