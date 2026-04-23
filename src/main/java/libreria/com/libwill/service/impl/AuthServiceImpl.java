package libreria.com.libwill.service.impl;

import libreria.com.libwill.dto.JwtResponseDTO;
import libreria.com.libwill.dto.LoginRequestDTO;
import libreria.com.libwill.dto.RegisterRequestDTO;
import libreria.com.libwill.entity.RolEntity;
import libreria.com.libwill.entity.UsuarioEntity;
import libreria.com.libwill.repository.RolRepository;
import libreria.com.libwill.repository.UsuarioRepository;
import libreria.com.libwill.security.jwt.JwtUtils;
import libreria.com.libwill.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Implementación del servicio de autenticación
 */
@Service
@Transactional
public class AuthServiceImpl implements AuthService {


    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private UsuarioRepository usuarioRepository;


    @Autowired
    private RolRepository rolRepository;


    @Autowired
    private PasswordEncoder encoder;


    @Autowired
    private JwtUtils jwtUtils;


    /**
     * Autentica un usuario y genera un token JWT
     */
    @Override
    public JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );


            SecurityContextHolder.getContext().setAuthentication(authentication);


            String jwt = jwtUtils.generateJwtToken(authentication);


            UsuarioEntity userDetails = (UsuarioEntity) authentication.getPrincipal();


            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .toList();


            return JwtResponseDTO.builder()
                    .token(jwt)
                    .type("Bearer")
                    .id(userDetails.getId())
                    .username(userDetails.getUsername())
                    .email(userDetails.getEmail())
                    .nombre(userDetails.getNombre())
                    .apellido(userDetails.getApellido())
                    .direccion(userDetails.getDireccion())
                    .numero(userDetails.getNumero())
                    .roles(roles)
                    .build();


        } catch (Exception e) {
            System.out.println("❌ Error durante autenticación: " + e.getMessage());
            e.printStackTrace();
            throw e; // Para que lo capture JwtAuthEntryPoint
        }
    }




    /**
     * Registra un nuevo usuario en el sistema
     */
    @Override
    public String registerUser(RegisterRequestDTO registerRequest) {
        // Verificar si el username ya existe
        if (usuarioRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Error: El nombre de usuario ya está en uso!");
        }


        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Error: El email ya está en uso!");
        }


        // Crear nuevo usuario
        UsuarioEntity user = new UsuarioEntity();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        user.setNombre(registerRequest.getNombre());
        user.setApellido(registerRequest.getApellido());
        user.setDireccion(registerRequest.getDireccion());
        user.setNumero(registerRequest.getNumero());
        user.setActivo(true);


        // Asignar roles
        Set<String> strRoles = registerRequest.getRoles();
        Set<RolEntity> roles = new HashSet<>();


        if (strRoles == null || strRoles.isEmpty()) {
            // Si no se especifican roles, asignar rol USER por defecto
            RolEntity userRole = rolRepository.findByNombre("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Rol USER no encontrado."));
            roles.add(userRole);
        } else {
            // Asignar los roles especificados
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        RolEntity adminRole = rolRepository.findByNombre("ROLE_ADMIN")
                                .orElseThrow(() -> new RuntimeException("Error: Rol ADMIN no encontrado."));
                        roles.add(adminRole);
                        break;
                    case "vendedor":
                        RolEntity vendedorRole = rolRepository.findByNombre("ROLE_VENDEDOR")
                                .orElseThrow(() -> new RuntimeException("Error: Rol VENDEDOR no encontrado."));
                        roles.add(vendedorRole);
                        break;
                    default:
                        RolEntity userRole = rolRepository.findByNombre("ROLE_USER")
                                .orElseThrow(() -> new RuntimeException("Error: Rol USER no encontrado."));
                        roles.add(userRole);
                }
            });
        }


        user.setRoles(roles);
        usuarioRepository.save(user);


        return "Usuario registrado exitosamente!";
    }


    /**
     * Verifica si un username está disponible
     */
    @Override
    public boolean isUsernameAvailable(String username) {
        return !usuarioRepository.existsByUsername(username);
    }


    /**
     * Verifica si un email está disponible
     */
    @Override
    public boolean isEmailAvailable(String email) {
        return !usuarioRepository.existsByEmail(email);
    }
}