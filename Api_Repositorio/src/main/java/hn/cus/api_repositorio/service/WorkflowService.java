/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hn.cus.api_repositorio.service;

/**
 *
 * @author EG490082
 */

import java.util.List;
import hn.cus.api_repositorio.entity.WorkflowEtapa;

public interface WorkflowService {
    List<WorkflowEtapa> obtenerEtapas(Long workflowId);
}
