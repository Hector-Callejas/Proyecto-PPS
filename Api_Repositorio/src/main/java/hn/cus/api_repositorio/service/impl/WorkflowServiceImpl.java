/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.cus.api_repositorio.service.impl;

/**
 *
 * @author EG490082
 */
import hn.cus.api_repositorio.entity.WorkflowEtapa;
import hn.cus.api_repositorio.repository.WorkflowEtapaRepository;
import hn.cus.api_repositorio.service.WorkflowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired private WorkflowEtapaRepository workflowEtapaRepository;

    @Override
    public List<WorkflowEtapa> obtenerEtapas(Long workflowId) {
        return workflowEtapaRepository.findByWorkflow_IdOrderByOrdenAsc(workflowId);
    }
}
