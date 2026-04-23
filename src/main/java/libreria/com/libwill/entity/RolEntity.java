package libreria.com.libwill.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Set;


/**
 * Entidad que representa los roles del sistema
 * Ejemplo: ROLE_ADMIN, ROLE_USER, ROLE_VENDEDOR
 */
@Data
@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class RolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true, length = 50)
    private String nombre;


    @Column(length = 200)
    private String descripcion;


    // Constructor auxiliar
    public RolEntity(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
}
