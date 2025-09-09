/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.cus.api_repositorio.controller;

import hn.cus.api_repositorio.entity.Documento;
import hn.cus.api_repositorio.service.DocumentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import hn.cus.api_repositorio.dto.DocumentoBase64Request;
import hn.cus.api_repositorio.dto.DocumentoResponseDTO;
import hn.cus.api_repositorio.config.FileStorageConfig;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.HashMap;

/**
 *
 * @author EG490082
 */
@RestController
@RequestMapping("/api/documentos")
@Tag(name = "Documentos", description = "Operaciones sobre documentos")
@Validated
public class DocumentoController {

    @Autowired private DocumentoService documentoService;
    @Autowired private FileStorageConfig fileStorageConfig;

    @Operation(summary = "Subir documento", description = "Permite subir un archivo y registrar su información")
    @PostMapping(value = "/subir", consumes = "multipart/form-data")
    public ResponseEntity<Documento> subirDocumento(@RequestParam("file") @NotNull(message = "El archivo es obligatorio") MultipartFile archivo,
                                                    @RequestParam("usuarioId") @NotNull(message = "El ID del usuario es obligatorio") Long usuarioId) {
        System.out.println("Recibiendo archivo: " + archivo.getOriginalFilename());
        System.out.println("Tamaño del archivo: " + archivo.getSize());
        System.out.println("Usuario ID: " + usuarioId);
        
        Documento doc = documentoService.guardarDocumento(archivo, usuarioId);
        return ResponseEntity.ok(doc);
    }

    @Operation(summary = "Health check", description = "Verifica que el controlador de documentos esté funcionando")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("DocumentoController funcionando correctamente");
    }

    @Operation(summary = "Test endpoint", description = "Endpoint de prueba para verificar funcionamiento")
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "Backend funcionando correctamente");
        response.put("timestamp", java.time.LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Test documento simple", description = "Endpoint de prueba para documento")
    @GetMapping("/{id}/test")
    public ResponseEntity<?> testDocumento(@PathVariable("id") Long id) {
        try {
            System.out.println("=== TEST DOCUMENTO ===");
            System.out.println("ID recibido: " + id);
            
            // Solo verificar si existe
            boolean existe = documentoService.existeDocumento(id);
            System.out.println("Documento existe: " + existe);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", id);
            response.put("existe", existe);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("ERROR en test: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Contar documentos", description = "Devuelve el número de documentos almacenados")
    @GetMapping("/count")
    public ResponseEntity<Long> contarDocumentos() {
        try {
            long count = documentoService.listarTodos().size();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            System.err.println("Error al contar documentos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "Listar documentos", description = "Devuelve una lista de documentos almacenados")
    @GetMapping
    public ResponseEntity<List<DocumentoResponseDTO>> listarDocumentos() {
        try {
            System.out.println("Obteniendo lista de documentos...");
            List<Documento> documentos = documentoService.listarTodos();
            System.out.println("Documentos encontrados: " + documentos.size());
            
            // Convertir a DTOs para evitar problemas de serialización
            List<DocumentoResponseDTO> documentosDTO = documentos.stream()
                    .map(DocumentoResponseDTO::new)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(documentosDTO);
        } catch (Exception e) {
            System.err.println("Error al listar documentos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "Obtener documento por ID", description = "Devuelve un documento específico por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDocumento(@PathVariable("id") Long id) {
        try {
            System.out.println("=== INICIO obtenerDocumento ===");
            System.out.println("Obteniendo documento con ID: " + id);
            
            // Paso 1: Verificar si existe
            System.out.println("Paso 1: Verificando existencia...");
            boolean existe = documentoService.existeDocumento(id);
            System.out.println("Documento existe: " + existe);
            
            if (!existe) {
                System.out.println("Documento no encontrado con ID: " + id);
                return ResponseEntity.notFound().build();
            }
            
            // Paso 2: Obtener documento
            System.out.println("Paso 2: Obteniendo documento...");
            Documento documento = documentoService.obtenerPorId(id);
            System.out.println("Documento obtenido: " + documento.getNombreOriginal());
            
            // Paso 3: Crear respuesta básica
            System.out.println("Paso 3: Creando respuesta...");
            Map<String, Object> response = new HashMap<>();
            response.put("id", documento.getId());
            response.put("nombreOriginal", documento.getNombreOriginal());
            response.put("tipoMime", documento.getTipoMime());
            response.put("rutaArchivo", documento.getRutaArchivo());
            response.put("publico", documento.getPublico());
            response.put("fechaCreacion", documento.getFechaCreacion() != null ? documento.getFechaCreacion().toString() : null);
            response.put("idTarea", documento.getIdTarea());
            response.put("username", "admin");
            
            System.out.println("Respuesta creada exitosamente");
            System.out.println("=== FIN obtenerDocumento ===");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("=== ERROR en obtenerDocumento ===");
            System.err.println("Error al obtener documento con ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            System.err.println("=== FIN ERROR ===");
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Obtener contenido Base64 de documento", description = "Devuelve el contenido Base64 de un documento")
    @GetMapping("/{id}/content")
    public ResponseEntity<?> obtenerContenidoDocumento(@PathVariable("id") Long id) {
        try {
            System.out.println("=== INICIO obtenerContenidoDocumento ===");
            System.out.println("Obteniendo contenido del documento con ID: " + id);
            
            // Verificar si existe primero
            if (!documentoService.existeDocumento(id)) {
                System.out.println("Documento no encontrado con ID: " + id);
                return ResponseEntity.notFound().build();
            }
            
            // Obtener documento
            Documento documento = documentoService.obtenerPorId(id);
            System.out.println("Documento encontrado: " + documento.getNombreOriginal());
            
            // Verificar si tiene contenido Base64
            if (documento.getContenidoBase64() == null || documento.getContenidoBase64().isEmpty()) {
                System.out.println("Documento no tiene contenido Base64");
                return ResponseEntity.badRequest().body(Map.of("error", "Documento no tiene contenido Base64"));
            }
            
            // Crear respuesta con el contenido Base64
            Map<String, Object> response = new HashMap<>();
            response.put("content", documento.getContenidoBase64());
            response.put("tipoMime", documento.getTipoMime());
            response.put("nombreOriginal", documento.getNombreOriginal());
            
            System.out.println("Contenido Base64 obtenido exitosamente");
            System.out.println("=== FIN obtenerContenidoDocumento ===");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("=== ERROR en obtenerContenidoDocumento ===");
            System.err.println("Error al obtener contenido del documento con ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            System.err.println("=== FIN ERROR ===");
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar documento", description = "Elimina un documento por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDocumento(@PathVariable("id") Long id) {
        documentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Subir documento Base64", description = "Sube un documento en formato Base64")
    @PostMapping("/uploadBase64")
    public ResponseEntity<Documento> subirDocumentoBase64(@RequestBody DocumentoBase64Request request) {
        Documento doc = documentoService.guardarDocumentoBase64(request);
        return ResponseEntity.ok(doc);
    }

    @Operation(summary = "Descargar documento", description = "Descarga un documento por su ID")
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> descargarDocumento(@PathVariable("id") Long id) {
        try {
            System.out.println("Descargando documento con ID: " + id);
            Documento documento = documentoService.obtenerPorId(id);
            System.out.println("Documento encontrado para descarga: " + documento.getNombreOriginal());
            System.out.println("Ruta del archivo: " + documento.getRutaArchivo());
            
            if (documento.getRutaArchivo().startsWith("base64://")) {
                // Archivo guardado como Base64
                if (documento.getContenidoBase64() == null) {
                    return ResponseEntity.notFound().build();
                }
                
                // Convertir Base64 a bytes
                byte[] fileBytes = java.util.Base64.getDecoder().decode(documento.getContenidoBase64());
                
                // Crear Resource desde bytes
                Resource resource = new org.springframework.core.io.ByteArrayResource(fileBytes);
                
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(documento.getTipoMime()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, 
                                "attachment; filename=\"" + documento.getNombreOriginal() + "\"")
                        .body(resource);
                        
            } else {
                // Archivo guardado físicamente
                Path filePath = Paths.get(fileStorageConfig.getUploadDir())
                        .resolve(documento.getRutaArchivo().replace("/uploads/", ""));
                
                Resource resource = new UrlResource(filePath.toUri());
                
                if (!resource.exists()) {
                    return ResponseEntity.notFound().build();
                }
                
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(documento.getTipoMime()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, 
                                "attachment; filename=\"" + documento.getNombreOriginal() + "\"")
                        .body(resource);
            }
            
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("Error al descargar documento: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}