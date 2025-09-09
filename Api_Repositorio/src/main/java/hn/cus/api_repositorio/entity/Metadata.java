/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.cus.api_repositorio.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 *
 * @author EG490082
 */
// Metadata
@Entity
@Table(name = "metadata", schema = "gestdoc_ow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id")
    private Documento documento;

    @Column(nullable = false, length = 100)
    private String clave;
    
    private String valor;
}

