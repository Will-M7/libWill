package libreria.com.libwill.controller;

import libreria.com.libwill.dto.JwtResponseDTO;
import libreria.com.libwill.dto.LoginRequestDTO;
import libreria.com.libwill.dto.RegisterRequestDTO;
import libreria.com.libwill.dto.response.UsuarioInfoDTO;
import libreria.com.libwill.entity.UsuarioEntity;
import libreria.com.libwill.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controlador para manejo de autenticación (login y registro)
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {


    @Autowired
    private AuthService authService;


    /**
     * Endpoint de inicio de sesión
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        JwtResponseDTO jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }


    /**
     * Endpoint de registro de usuario
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO registerRequest) {
        String message = authService.registerUser(registerRequest);
        return ResponseEntity.ok(message);
    }


    /**
     * Verificar disponibilidad de username
     */
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Boolean> checkUsername(@PathVariable String username) {
        boolean available = authService.isUsernameAvailable(username);
        return ResponseEntity.ok(available);
    }


    /**
     * Verificar disponibilidad de email
     */
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> checkEmail(@PathVariable String email) {
        boolean available = authService.isEmailAvailable(email);
        return ResponseEntity.ok(available);
    }
    @Autowired
    private libreria.com.libwill.repository.UsuarioRepository usuarioRepository; // Inyecta el repositorio

    /**
     * Listar todos los usuarios (Solo para ADMIN/VENDEDOR según SecurityConfig)
     */
    @GetMapping
    public ResponseEntity<List<UsuarioInfoDTO>> listarUsuarios() {
        List<UsuarioEntity> usuarios = usuarioRepository.findAll();

        List<libreria.com.libwill.dto.response.UsuarioInfoDTO> response = usuarios.stream().map(u -> {
            libreria.com.libwill.dto.response.UsuarioInfoDTO dto = new libreria.com.libwill.dto.response.UsuarioInfoDTO();
            dto.setId(u.getId());
            dto.setUsername(u.getUsername());
            dto.setEmail(u.getEmail());
            dto.setNombre(u.getNombre());
            dto.setApellido(u.getApellido());
            // Agregamos los campos extra que tienes en la entidad
            dto.setDireccion(u.getDireccion());
            dto.setNumero(u.getNumero());

            // Mapeamos roles
            dto.setRoles(u.getRoles().stream().map(r -> r.getNombre()).collect(java.util.stream.Collectors.toList()));
            return dto;
        }).collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
