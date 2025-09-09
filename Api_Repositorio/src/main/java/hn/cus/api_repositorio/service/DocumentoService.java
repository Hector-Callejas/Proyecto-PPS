/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hn.cus.api_repositorio.service;

/**
 *
 * @author EG490082
 */
// DocumentoService.java

import hn.cus.api_repositorio.entity.Documento;
import hn.cus.api_repositorio.dto.DocumentoBase64Request;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface DocumentoService {
    Documento guardarDocumento(MultipartFile archivo, Long usuarioId);
    Documento guardarDocumentoBase64(MultipartFile archivo, Long usuarioId, String comentario, Long idTarea);
    Documento guardarDocumentoBase64(DocumentoBase64Request request);
    List<Documento> listarTodos();
    Documento obtenerPorId(Long id);
    boolean existeDocumento(Long id);
    void eliminar(Long id);
}

