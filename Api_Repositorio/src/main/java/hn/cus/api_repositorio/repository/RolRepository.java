/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.cus.api_repositorio.repository;

import hn.cus.api_repositorio.entity.AuditLog;
import hn.cus.api_repositorio.entity.Documento;
import hn.cus.api_repositorio.entity.DocumentoVersion;
import hn.cus.api_repositorio.entity.DocumentoWorkflow;
import hn.cus.api_repositorio.entity.Metadata;
import hn.cus.api_repositorio.entity.Rol;
import hn.cus.api_repositorio.entity.Workflow;
import hn.cus.api_repositorio.entity.WorkflowEtapa;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author EG490082
 */
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}

