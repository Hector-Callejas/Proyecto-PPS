package hn.cus.api_repositorio.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "permisos", schema = "gestdoc_ow")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "El nombre del permiso es obligatorio")
    @Size(max = 50, message = "El nombre del permiso no puede exceder 50 caracteres")
    @Column(nullable = false, length = 50, unique = true)
    private String nombre;

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    @Column(length = 255)
    private String descripcion;

    // Si más adelante necesitas navegar desde Permiso a Rol:
    // @ManyToMany(mappedBy = "permisos", fetch = FetchType.LAZY)
    // private Set<Rol> roles = new HashSet<>();
}
