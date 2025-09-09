/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.cus.api_repositorio.controller;

import hn.cus.api_repositorio.service.WorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author EG490082
 */
@RestController
@RequestMapping("/api/workflows")
@Tag(name = "Workflows", description = "Operaciones sobre flujos de trabajo")
public class WorkflowController {

    @Autowired private WorkflowService workflowService;

    @Operation(summary = "Obtener etapas del workflow", description = "Lista todas las etapas de un workflow dado su ID")
    @GetMapping("/{id}/etapas")
    public ResponseEntity<?> obtenerEtapas(@PathVariable Long id) {
        return ResponseEntity.ok(workflowService.obtenerEtapas(id));
    }
}
