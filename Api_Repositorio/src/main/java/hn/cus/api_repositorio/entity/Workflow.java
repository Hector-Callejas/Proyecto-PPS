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

// Workflow
@Entity
@Table(name = "workflows", schema = "gestdoc_ow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Workflow {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    private String descripcion;
}

