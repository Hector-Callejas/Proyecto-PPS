/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hn.cus.api_repositorio.repository;

import hn.cus.api_repositorio.entity.DocumentoWorkflow;
import hn.cus.api_repositorio.entity.Workflow;
import hn.cus.api_repositorio.entity.WorkflowEtapa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author EG490082
 */
public interface WorkflowRepository extends JpaRepository<Workflow, Long> {
}

