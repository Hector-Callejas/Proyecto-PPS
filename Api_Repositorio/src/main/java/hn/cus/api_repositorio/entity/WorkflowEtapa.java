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
// WorkflowEtapa
@Entity
@Table(name = "workflow_etapas", schema = "gestdoc_ow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowEtapa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;

    @Column(nullable = false)
    private Integer orden;
    
    private String descripcion;
}