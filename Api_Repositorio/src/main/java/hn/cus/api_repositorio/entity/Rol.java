package hn.cus.api_repositorio.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles", schema = "gestdoc_ow")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "permisos")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 50, message = "El nombre del rol no puede exceder 50 caracteres")
    @Column(nullable = false, length = 50, unique = true)
    private String nombre;

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    @Column(length = 255)
    private String descripcion;

    // ⚠️ Sin CascadeType.PERSIST para no intentar persistir permisos existentes
    @ManyToMany(
        cascade = { CascadeType.MERGE, CascadeType.REFRESH },
        fetch = FetchType.LAZY
    )
    @JoinTable(
        name = "rol_permiso",
        schema = "gestdoc_ow",
        joinColumns = @JoinColumn(name = "rol_id"),
        inverseJoinColumns = @JoinColumn(name = "permiso_id")
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Permiso> permisos = new HashSet<>();
}
