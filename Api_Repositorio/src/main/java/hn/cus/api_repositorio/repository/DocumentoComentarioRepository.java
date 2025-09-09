/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hn.cus.api_repositorio.repository;

/**
 *
 * @author EG490082
 */
import hn.cus.api_repositorio.entity.DocumentoComentario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentoComentarioRepository extends JpaRepository<DocumentoComentario, Long> {

    // Obtener todos los comentarios de un documento por su ID
    List<DocumentoComentario> findByDocumento_Id(Long documentoId);
}
