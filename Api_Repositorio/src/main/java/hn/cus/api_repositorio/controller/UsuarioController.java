/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.cus.api_repositorio.controller;

import hn.cus.api_repositorio.dto.UsuarioDTO;
import hn.cus.api_repositorio.entity.Usuario;
import hn.cus.api_repositorio.entity.Rol;
import hn.cus.api_repositorio.repository.UsuarioRepository;
import hn.cus.api_repositorio.service.UsuarioService;
import hn.cus.api_repositorio.service.AuthorizationService;
import hn.cus.api_repositorio.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author EG490082
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con usuarios")
public class UsuarioController {

    @Autowired private UsuarioService usuarioService;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private AuthorizationService authorizationService;
    @Autowired private JwtUtil jwtUtil;

    // Método para extraer username del token
    private String getUsernameFromToken(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                return jwtUtil.extraerUsername(token);
            }
        } catch (Exception e) {
            System.err.println("Error extrayendo username del token: " + e.getMessage());
        }
        return null;
    }

    // Método para verificar si el usuario puede gestionar usuarios
    private ResponseEntity<?> checkManageUsersPermission(HttpServletRequest request) {
        String username = getUsernameFromToken(request);
        if (username == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
        }

        if (!authorizationService.canManageUsers(username)) {
            return ResponseEntity.status(403).body(Map.of(
                "error", "No tienes permisos para gestionar usuarios",
                "username", username,
                "requiredPermission", "MANAGE_USERS"
            ));
        }

        return null; // Usuario autorizado
    }

    @Operation(summary = "Obtener usuario", description = "Devuelve los datos de un usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable("id") Long id, HttpServletRequest request) {
        // Verificar permisos
        ResponseEntity<?> authCheck = checkManageUsersPermission(request);
        if (authCheck != null) {
            return authCheck;
        }
        try {
            System.out.println("=== INICIO obtenerUsuario ===");
            System.out.println("Obteniendo usuario con ID: " + id);
            
            UsuarioDTO usuario = usuarioService.obtenerUsuarioDTO(id);
            System.out.println("Usuario obtenido: " + usuario.getUsername());
            
            System.out.println("=== FIN obtenerUsuario ===");
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            System.err.println("=== ERROR en obtenerUsuario ===");
            System.err.println("Error al obtener usuario con ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            System.err.println("=== FIN ERROR ===");
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al obtener usuario: " + e.getMessage());
            errorResponse.put("id", id);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios")
    @GetMapping
    public ResponseEntity<?> obtenerUsuarios(HttpServletRequest request) {
        // Verificar permisos
        ResponseEntity<?> authCheck = checkManageUsersPermission(request);
        if (authCheck != null) {
            return authCheck;
        }

        return ResponseEntity.ok(usuarioService.obtenerTodosUsuarios());
    }

    @Operation(summary = "Crear nuevo usuario", description = "Crea un nuevo usuario en el sistema")
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioDTO usuarioDTO, HttpServletRequest request) {
        // Verificar permisos
        ResponseEntity<?> authCheck = checkManageUsersPermission(request);
        if (authCheck != null) {
            return authCheck;
        }

        return ResponseEntity.ok(usuarioService.crearUsuario(usuarioDTO));
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable("id") Long id, @RequestBody UsuarioDTO usuarioDTO, HttpServletRequest request) {
        // Verificar permisos
        ResponseEntity<?> authCheck = checkManageUsersPermission(request);
        if (authCheck != null) {
            return authCheck;
        }

        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, usuarioDTO));
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable("id") Long id, HttpServletRequest request) {
        // Verificar permisos
        ResponseEntity<?> authCheck = checkManageUsersPermission(request);
        if (authCheck != null) {
            return authCheck;
        }

        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Test endpoint", description = "Endpoint de prueba para verificar funcionamiento")
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "UsuarioController funcionando correctamente");
        response.put("timestamp", java.time.LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Test usuario simple", description = "Endpoint de prueba para obtener usuario básico")
    @GetMapping("/{id}/simple")
    public ResponseEntity<?> obtenerUsuarioSimple(@PathVariable("id") Long id) {
        try {
            System.out.println("=== INICIO obtenerUsuarioSimple ===");
            System.out.println("Obteniendo usuario simple con ID: " + id);
            
            // Obtener usuario directamente del repositorio
            Usuario usuario = usuarioRepository.findById(id).orElse(null);
            
            if (usuario == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Usuario no encontrado");
                errorResponse.put("id", id);
                return ResponseEntity.status(404).body(errorResponse);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", usuario.getId());
            response.put("username", usuario.getUsername());
            response.put("email", usuario.getEmail());
            response.put("activo", usuario.getActivo());
            response.put("roles_count", usuario.getRoles() != null ? usuario.getRoles().size() : 0);
            
            System.out.println("Usuario simple obtenido: " + usuario.getUsername());
            System.out.println("=== FIN obtenerUsuarioSimple ===");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("=== ERROR en obtenerUsuarioSimple ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=== FIN ERROR ===");
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al obtener usuario: " + e.getMessage());
            errorResponse.put("id", id);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Operation(summary = "Debug usuario", description = "Endpoint de debug completamente público")
    @GetMapping("/debug/{id}")
    public ResponseEntity<?> debugUsuario(@PathVariable("id") Long id) {
        try {
            System.out.println("=== DEBUG USUARIO ===");
            System.out.println("ID solicitado: " + id);
            
            // Verificar si el usuario existe
            boolean exists = usuarioRepository.existsById(id);
            System.out.println("Usuario existe: " + exists);
            
            if (!exists) {
                return ResponseEntity.ok(Map.of(
                    "error", "Usuario no encontrado",
                    "id", id,
                    "exists", false
                ));
            }
            
            // Obtener usuario básico
            Usuario usuario = usuarioRepository.findById(id).orElse(null);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("id", usuario.getId());
            response.put("username", usuario.getUsername());
            response.put("email", usuario.getEmail());
            response.put("activo", usuario.getActivo());
            response.put("roles_count", usuario.getRoles() != null ? usuario.getRoles().size() : 0);
            
            if (usuario.getRoles() != null) {
                List<String> roles = new ArrayList<>();
                for (Rol rol : usuario.getRoles()) {
                    roles.add(rol.getNombre());
                }
                response.put("roles", roles);
            }
            
            System.out.println("Debug completado exitosamente");
            System.out.println("=== FIN DEBUG ===");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("=== ERROR EN DEBUG ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=== FIN ERROR ===");
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error en debug: " + e.getMessage());
            errorResponse.put("id", id);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Operation(summary = "Endpoint público temporal", description = "Endpoint público para debugging sin autenticación")
    @GetMapping("/public/{id}")
    public ResponseEntity<?> obtenerUsuarioPublico(@PathVariable("id") Long id) {
        try {
            System.out.println("=== ENDPOINT PUBLICO ===");
            System.out.println("Obteniendo usuario público con ID: " + id);
            
            // Obtener usuario directamente del repositorio
            Usuario usuario = usuarioRepository.findById(id).orElse(null);
            
            if (usuario == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Usuario no encontrado");
                response.put("id", id);
                return ResponseEntity.ok(response);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("id", usuario.getId());
            response.put("username", usuario.getUsername());
            response.put("email", usuario.getEmail());
            response.put("activo", usuario.getActivo());
            
            // Obtener roles de forma segura
            List<String> roles = new ArrayList<>();
            if (usuario.getRoles() != null) {
                for (Rol rol : usuario.getRoles()) {
                    roles.add(rol.getNombre());
                }
            }
            response.put("roles", roles);
            
            System.out.println("Usuario público obtenido exitosamente: " + usuario.getUsername());
            System.out.println("=== FIN ENDPOINT PUBLICO ===");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("=== ERROR EN ENDPOINT PUBLICO ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=== FIN ERROR ===");
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error en endpoint público: " + e.getMessage());
            errorResponse.put("id", id);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
