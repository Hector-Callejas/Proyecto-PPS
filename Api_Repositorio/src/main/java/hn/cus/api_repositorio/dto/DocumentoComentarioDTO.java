/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.cus.api_repositorio.dto;

/**
 *
 * @author EG490082
 */
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoComentarioDTO {
    private Long id;
    private String comentario;
    private Long creadoPorId;
    private String creadoPorUsername;
    private LocalDateTime fechaCreacion;
}

