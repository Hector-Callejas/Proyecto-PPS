/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.cus.api_repositorio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author EG490082
 */
@Data
public class DocumentoBase64Request {
    
    @NotBlank(message = "El nombre del archivo es obligatorio")
    private String fileName;
    
    @NotBlank(message = "El tipo de archivo es obligatorio")
    private String fileType;
    
    @NotBlank(message = "El contenido Base64 es obligatorio")
    private String contentBase64;
    
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;
    
    private String comentario;
    private Long idTarea;
}
