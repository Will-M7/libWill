package libreria.com.libwill.util;

import jakarta.annotation.PostConstruct;
import libreria.com.libwill.entity.RolEntity;
import libreria.com.libwill.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Carga los roles por defecto al iniciar la aplicación
 */
@Component
public class DataInitializer {


    @Autowired
    private RolRepository rolRepository;


    @PostConstruct
    public void init() {
        createRoleIfNotExists("ROLE_ADMIN", "Administrador del sistema");
        createRoleIfNotExists("ROLE_USER", "Usuario estándar del sistema");
        createRoleIfNotExists("ROLE_VENDEDOR", "Usuario con permisos de venta");
    }


    private void createRoleIfNotExists(String nombre, String descripcion) {
        rolRepository.findByNombre(nombre).orElseGet(() -> {
            RolEntity rol = new RolEntity();
            rol.setNombre(nombre);
            rol.setDescripcion(descripcion);
            rolRepository.save(rol);
            System.out.println("✅ Rol creado: " + nombre);
            return rol;
        });
    }
}
