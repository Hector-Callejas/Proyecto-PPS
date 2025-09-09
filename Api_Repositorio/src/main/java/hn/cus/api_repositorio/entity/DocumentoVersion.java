/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.cus.api_repositorio.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 *
 * @author EG490082
 */
// DocumentoVersion
@Entity
@Table(name = "documento_version", schema = "gestdoc_ow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoVersion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id")
    private Documento documento;

    @Column(nullable = false)
    private Integer version;
    
    @Column(nullable = false)
    private String rutaArchivo;
    
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por")
    private Usuario creadoPor;
}


