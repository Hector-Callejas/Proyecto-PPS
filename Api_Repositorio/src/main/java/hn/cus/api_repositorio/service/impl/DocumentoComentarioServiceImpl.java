/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.cus.api_repositorio.service.impl;

/**
 *
 * @author EG490082
 */

import hn.cus.api_repositorio.dto.DocumentoComentarioDTO;
import hn.cus.api_repositorio.entity.*;
import hn.cus.api_repositorio.repository.*;
import hn.cus.api_repositorio.service.DocumentoComentarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentoComentarioServiceImpl implements DocumentoComentarioService {

    @Autowired private DocumentoRepository documentoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private DocumentoComentarioRepository documentoComentarioRepository;

    @Override
    public DocumentoComentarioDTO agregarComentario(Long documentoId, Long usuarioId, String comentario) {
        Documento documento = documentoRepository.findById(documentoId)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        DocumentoComentario nuevoComentario = new DocumentoComentario();
        nuevoComentario.setDocumento(documento);
        nuevoComentario.setComentario(comentario);
        nuevoComentario.setCreadoPor(usuario);

        DocumentoComentario guardado = documentoComentarioRepository.save(nuevoComentario);

        return new DocumentoComentarioDTO(
                guardado.getId(),
                guardado.getComentario(),
                usuario.getId(),
                usuario.getUsername(),
                guardado.getFechaCreacion()
        );
    }

    @Override
    public List<DocumentoComentarioDTO> listarComentarios(Long documentoId) {
        return documentoComentarioRepository.findByDocumento_Id(documentoId)
                .stream()
                .map(c -> new DocumentoComentarioDTO(
                        c.getId(),
                        c.getComentario(),
                        c.getCreadoPor().getId(),
                        c.getCreadoPor().getUsername(),
                        c.getFechaCreacion()))
                .collect(Collectors.toList());
    }
}
