/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hn.cus.api_repositorio.repository;

import hn.cus.api_repositorio.entity.DocumentoWorkflow;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author EG490082
 */
public interface DocumentoWorkflowRepository extends JpaRepository<DocumentoWorkflow, Long> {
    List<DocumentoWorkflow> findByDocumento_Id(Long documentoId);
}

