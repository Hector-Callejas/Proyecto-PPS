package hn.cus.api_repositorio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioRequestDTO {
    
    @NotNull(message = "El ID del documento es obligatorio")
    private Long documentoId;
    
    @NotBlank(message = "El comentario no puede estar vacío")
    private String comentario;
}
