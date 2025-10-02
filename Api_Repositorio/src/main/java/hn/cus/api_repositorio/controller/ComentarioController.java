package hn.cus.api_repositorio.controller;

import hn.cus.api_repositorio.dto.ComentarioRequestDTO;
import hn.cus.api_repositorio.dto.DocumentoComentarioDTO;
import hn.cus.api_repositorio.entity.Usuario;
import hn.cus.api_repositorio.repository.UsuarioRepository;
import hn.cus.api_repositorio.security.JwtUtil;
import hn.cus.api_repositorio.service.DocumentoComentarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/comentarios")
@Tag(name = "Comentarios", description = "Operaciones relacionadas con comentarios de documentos")
public class ComentarioController {

    @Autowired
    private DocumentoComentarioService documentoComentarioService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Operation(summary = "Agregar comentario", description = "Agrega un comentario a un documento")
    @PostMapping
    public ResponseEntity<?> agregarComentario(@RequestBody ComentarioRequestDTO request, HttpServletRequest httpRequest) {
        try {
            // Obtener el ID del usuario desde el token JWT
            String token = jwtUtil.extractTokenFromRequest(httpRequest);
            
            // Validar que el token existe
            if (token == null || token.trim().isEmpty()) {
                return ResponseEntity.status(401)
                    .body(Map.of("error", "Token de autorización requerido"));
            }
            
            Long usuarioId = jwtUtil.getUserIdFromToken(token);
            
            // Validar que el usuario existe
            Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
            if (!usuario.isPresent()) {
                return ResponseEntity.status(404)
                    .body(Map.of("error", "Usuario no encontrado"));
            }
            
            DocumentoComentarioDTO comentario = documentoComentarioService.agregarComentario(
                request.getDocumentoId(), 
                usuarioId, 
                request.getComentario()
            );
            
            return ResponseEntity.ok(comentario);
            
        } catch (Exception e) {
            System.err.println("Error al agregar comentario: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Error al agregar comentario: " + e.getMessage()));
        }
    }

    @Operation(summary = "Obtener comentarios de documento", description = "Obtiene todos los comentarios de un documento específico")
    @GetMapping("/documento/{documentoId}")
    public ResponseEntity<?> obtenerComentarios(@PathVariable("documentoId") Long documentoId) {
        try {
            List<DocumentoComentarioDTO> comentarios = documentoComentarioService.listarComentarios(documentoId);
            return ResponseEntity.ok(comentarios);
            
        } catch (Exception e) {
            System.err.println("Error al obtener comentarios: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Error al obtener comentarios: " + e.getMessage()));
        }
    }

    @Operation(summary = "Obtener conteo de comentarios", description = "Obtiene el número de comentarios de un documento")
    @GetMapping("/documento/{documentoId}/count")
    public ResponseEntity<?> obtenerConteoComentarios(@PathVariable("documentoId") Long documentoId) {
        try {
            List<DocumentoComentarioDTO> comentarios = documentoComentarioService.listarComentarios(documentoId);
            return ResponseEntity.ok(Map.of("count", comentarios.size()));
            
        } catch (Exception e) {
            System.err.println("Error al obtener conteo de comentarios: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Error al obtener conteo de comentarios: " + e.getMessage()));
        }
    }
}
