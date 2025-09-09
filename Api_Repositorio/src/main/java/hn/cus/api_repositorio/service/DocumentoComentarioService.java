/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hn.cus.api_repositorio.service;

/**
 *
 * @author EG490082
 */

import hn.cus.api_repositorio.dto.DocumentoComentarioDTO;
import java.util.List;

public interface DocumentoComentarioService {
    DocumentoComentarioDTO agregarComentario(Long documentoId, Long usuarioId, String comentario);
    List<DocumentoComentarioDTO> listarComentarios(Long documentoId);
}

