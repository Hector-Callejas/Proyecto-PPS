package hn.cus.api_repositorio.controller;

import hn.cus.api_repositorio.service.AuthorizationService;
import hn.cus.api_repositorio.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * Controlador para manejar la autorización y verificación de permisos
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autorización", description = "Verificación de permisos y autorización")
public class AuthorizationController {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Obtiene los permisos del usuario actual
     */
    @Operation(summary = "Obtener permisos del usuario", description = "Devuelve todos los permisos del usuario autenticado")
    @GetMapping("/permissions")
    public ResponseEntity<?> getUserPermissions(HttpServletRequest request) {
        try {
            // Extraer token del header
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Token de autorización no encontrado"));
            }

            String token = authHeader.substring(7);
            String username = jwtUtil.extraerUsername(token);

            Set<String> permissions = authorizationService.getUserPermissions(username);

            return ResponseEntity.ok(Map.of(
                "username", username,
                "permissions", permissions,
                "canManageRoles", authorizationService.canManageRoles(username),
                "canManageUsers", authorizationService.canManageUsers(username),
                "canUploadDocuments", authorizationService.canUploadDocuments(username)
            ));

        } catch (Exception e) {
            System.err.println("Error obteniendo permisos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                .body(Map.of("error", "Error al obtener permisos: " + e.getMessage()));
        }
    }

    /**
     * Verifica si el usuario tiene un permiso específico
     */
    @Operation(summary = "Verificar permiso", description = "Verifica si el usuario tiene un permiso específico")
    @GetMapping("/check-permission/{permission}")
    public ResponseEntity<?> checkPermission(
            @PathVariable("permission") String permission,
            HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Token de autorización no encontrado"));
            }

            String token = authHeader.substring(7);
            String username = jwtUtil.extraerUsername(token);

            boolean hasPermission = authorizationService.hasPermission(username, permission);

            return ResponseEntity.ok(Map.of(
                "username", username,
                "permission", permission,
                "hasPermission", hasPermission
            ));

        } catch (Exception e) {
            System.err.println("Error verificando permiso: " + e.getMessage());
            return ResponseEntity.status(500)
                .body(Map.of("error", "Error al verificar permiso: " + e.getMessage()));
        }
    }
}
