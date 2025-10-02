package hn.cus.api_repositorio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private List<String> permisos;
}

