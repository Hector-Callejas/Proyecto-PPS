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
public class DocumentoDTO {
    private Long id;
    private String nombreOriginal;
    private String tipoMime;
    private Boolean publico;
    private String rutaArchivo;       // (Opcional si aún subes archivos físicos)
    private String contenidoBase64;   // (Base64 completo del archivo)
    private Long idTarea;              // (Asociado a una tarea)
    private LocalDateTime fechaCreacion;
}

