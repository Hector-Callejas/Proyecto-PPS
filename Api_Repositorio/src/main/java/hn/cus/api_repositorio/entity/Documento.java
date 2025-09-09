/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.cus.api_repositorio.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Entity
@Table(name = "documentos", schema = "gestdoc_ow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre original es obligatorio")
    @Size(max = 255, message = "El nombre original no puede exceder 255 caracteres")
    @Column(nullable = false)
    private String nombreOriginal;
    
    private String tipoMime;
    
    @NotBlank(message = "La ruta del archivo es obligatoria")
    @Column(nullable = false)
    private String rutaArchivo;
    
    private Boolean publico;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creado_por")
    private Usuario creadoPor;

    private LocalDateTime fechaCreacion;

    @Lob
    @Column(name = "contenido_base64", columnDefinition = "TEXT")
    @JsonIgnore
    private String contenidoBase64;

    @Column(name = "id_tarea")
    private Long idTarea;
}
