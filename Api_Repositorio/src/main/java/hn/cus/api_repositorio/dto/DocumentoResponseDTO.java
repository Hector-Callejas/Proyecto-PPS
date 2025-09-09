/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.cus.api_repositorio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO para respuesta de documentos
 * @author EG490082
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoResponseDTO {
    
    private Long id;
    private String nombreOriginal;
    private String tipoMime;
    private String rutaArchivo;
    private Boolean publico;
    private String username; // Solo el nombre de usuario, no toda la entidad
    private LocalDateTime fechaCreacion;
    private Long idTarea;
    
    // Constructor para convertir desde entidad Documento
    public DocumentoResponseDTO(hn.cus.api_repositorio.entity.Documento documento) {
        this.id = documento.getId();
        this.nombreOriginal = documento.getNombreOriginal();
        this.tipoMime = documento.getTipoMime();
        this.rutaArchivo = documento.getRutaArchivo();
        this.publico = documento.getPublico();
        this.username = "admin"; // Temporalmente fijo para evitar problemas de relación
        this.fechaCreacion = documento.getFechaCreacion();
        this.idTarea = documento.getIdTarea();
    }
}
