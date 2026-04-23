package libreria.com.libwill.util;

import jakarta.annotation.PostConstruct;
import libreria.com.libwill.entity.RolEntity;
import libreria.com.libwill.entity.UsuarioEntity;
import libreria.com.libwill.repository.RolRepository;
import libreria.com.libwill.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DataLoader {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {

        // Crear el rol ADMIN si no existe
        RolEntity rolAdmin = rolRepository.findByNombre("ROLE_ADMIN")
                .orElseGet(() -> rolRepository.save(new RolEntity(null, "ROLE_ADMIN")));

        // Crear un único admin por defecto
        if (!usuarioRepository.existsByUsername("administrador")) {
            UsuarioEntity admin = new UsuarioEntity();
            admin.setUsername("administrador");
            admin.setEmail("adminde@libwill.com");
            admin.setPassword(passwordEncoder.encode("admin12345"));
            admin.setNombre("Administrador");
            admin.setApellido("Master");
            admin.setDireccion("Sistema");
            admin.setNumero("0");

            admin.setRoles(Collections.singleton(rolAdmin));

            usuarioRepository.save(admin);

            System.out.println("ADMIN CREADO POR DEFECTO: username=administrador password=admin12345");
        }
    }
}
