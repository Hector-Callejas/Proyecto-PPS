/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.cus.api_repositorio.service.impl;

/**
 *
 * @author EG490082
 */

import hn.cus.api_repositorio.entity.Documento;
import hn.cus.api_repositorio.entity.Usuario;
import hn.cus.api_repositorio.repository.DocumentoRepository;
import hn.cus.api_repositorio.repository.UsuarioRepository;
import hn.cus.api_repositorio.service.DocumentoService;
import hn.cus.api_repositorio.dto.DocumentoBase64Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import hn.cus.api_repositorio.config.FileStorageConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
public class DocumentoServiceImpl implements DocumentoService {

    @Autowired private DocumentoRepository documentoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private FileStorageConfig fileStorageConfig;

    @Override
    public Documento guardarDocumento(MultipartFile archivo, Long usuarioId) {
        if (archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));
        
        Documento doc = new Documento();
        doc.setNombreOriginal(archivo.getOriginalFilename());
        doc.setTipoMime(archivo.getContentType());
        doc.setFechaCreacion(LocalDateTime.now());
        doc.setCreadoPor(usuario);
        doc.setPublico(false);
        
        try {
            // Decidir si guardar como Base64 o físicamente
            if (archivo.getSize() <= fileStorageConfig.getMaxFileSizeBase64()) {
                // Archivo pequeño: guardar como Base64 en la base de datos
                doc.setContenidoBase64(convertirBase64(archivo));
                doc.setRutaArchivo("base64://" + archivo.getOriginalFilename());
                System.out.println("Archivo guardado como Base64: " + archivo.getOriginalFilename());
            } else {
                // Archivo grande: guardar físicamente en el servidor
                String fileName = System.currentTimeMillis() + "_" + archivo.getOriginalFilename();
                Path filePath = Paths.get(fileStorageConfig.getUploadDir()).resolve(fileName);
                
                // Crear directorio si no existe
                Files.createDirectories(filePath.getParent());
                
                // Guardar archivo físicamente
                Files.copy(archivo.getInputStream(), filePath);
                
                doc.setRutaArchivo("/uploads/" + fileName);
                System.out.println("Archivo guardado físicamente: " + filePath.toAbsolutePath());
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo: " + e.getMessage(), e);
        }
        
        return documentoRepository.save(doc);
    }

    @Override
    public Documento guardarDocumentoBase64(MultipartFile archivo, Long usuarioId, String comentario, Long idTarea) {
        if (archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));
        
        Documento doc = new Documento();
        doc.setNombreOriginal(archivo.getOriginalFilename());
        doc.setTipoMime(archivo.getContentType());
        doc.setFechaCreacion(LocalDateTime.now());
        doc.setCreadoPor(usuario);
        doc.setPublico(false);
        doc.setContenidoBase64(convertirBase64(archivo));
        doc.setIdTarea(idTarea);
        doc.setRutaArchivo("/uploads/" + System.currentTimeMillis() + "_" + archivo.getOriginalFilename());
        
        return documentoRepository.save(doc);
    }

    private String convertirBase64(MultipartFile file) {
        try {
            return Base64.getEncoder().encodeToString(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error al convertir archivo a Base64", e);
        }
    }

    @Override
    public Documento guardarDocumentoBase64(DocumentoBase64Request request) {
        if (request.getContentBase64() == null || request.getContentBase64().isEmpty()) {
            throw new IllegalArgumentException("El contenido Base64 no puede estar vacío");
        }
        
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + request.getUsuarioId()));
        
        Documento doc = new Documento();
        doc.setNombreOriginal(request.getFileName());
        doc.setTipoMime(request.getFileType());
        doc.setFechaCreacion(LocalDateTime.now());
        doc.setCreadoPor(usuario);
        doc.setPublico(false);
        doc.setContenidoBase64(request.getContentBase64());
        doc.setIdTarea(request.getIdTarea());
        doc.setRutaArchivo("/uploads/" + System.currentTimeMillis() + "_" + request.getFileName());
        
        return documentoRepository.save(doc);
    }

    @Override
    public List<Documento> listarTodos() {
        return documentoRepository.findAll();
    }

    @Override
    public Documento obtenerPorId(Long id) {
        return documentoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Documento no encontrado con ID: " + id));
    }

    @Override
    public boolean existeDocumento(Long id) {
        return documentoRepository.existsById(id);
    }

    @Override
    public void eliminar(Long id) {
        if (!documentoRepository.existsById(id)) {
            throw new IllegalArgumentException("Documento no encontrado con ID: " + id);
        }
        documentoRepository.deleteById(id);
    }
}
