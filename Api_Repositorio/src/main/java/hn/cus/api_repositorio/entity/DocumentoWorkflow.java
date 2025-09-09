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
// DocumentoWorkflow
@Entity
@Table(name = "documento_workflow", schema = "gestdoc_ow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoWorkflow {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id")
    private Documento documento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_actual")
    private WorkflowEtapa estadoActual;

    private Boolean aprobado = false;
}
