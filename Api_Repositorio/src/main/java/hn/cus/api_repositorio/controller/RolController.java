package hn.cus.api_repositorio.controller;

import hn.cus.api_repositorio.dto.RolDTO;
import hn.cus.api_repositorio.dto.RolResponseDTO;
import hn.cus.api_repositorio.entity.Permiso;
import hn.cus.api_repositorio.entity.Rol;
import hn.cus.api_repositorio.service.RolService;
import hn.cus.api_repositorio.service.AuthorizationService;
import hn.cus.api_repositorio.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author EG490082
 */
@RestController
@RequestMapping("/api/roles")
@Tag(name = "Roles", description = "Operaciones relacionadas con roles")
public class RolController {

    @Autowired
    private RolService rolService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private JwtUtil jwtUtil;

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

    // Método para verificar si el usuario puede gestionar roles
    private ResponseEntity<?> checkManageRolesPermission(HttpServletRequest request) {
        String username = getUsernameFromToken(request);
        if (username == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
        }

        if (!authorizationService.canManageRoles(username)) {
            return ResponseEntity.status(403).body(Map.of(
                "error", "No tienes permisos para gestionar roles",
                "username", username,
                "requiredPermission", "MANAGE_ROLES"
            ));
        }

        return null; // Usuario autorizado
    }

    // Método para convertir Rol a RolResponseDTO
    private RolResponseDTO convertirAResponseDTO(Rol rol) {
        RolResponseDTO dto = new RolResponseDTO();
        dto.setId(rol.getId());
        dto.setNombre(rol.getNombre());
        dto.setDescripcion(rol.getDescripcion());
        
        if (rol.getPermisos() != null) {
            dto.setPermisos(rol.getPermisos().stream()
                    .map(Permiso::getNombre)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }

    @Operation(summary = "Obtener todos los roles", description = "Devuelve una lista de todos los roles disponibles")
    @GetMapping
    public ResponseEntity<?> obtenerTodosLosRoles(HttpServletRequest request) {
        // Verificar permisos
        ResponseEntity<?> authCheck = checkManageRolesPermission(request);
        if (authCheck != null) {
            return authCheck;
        }

        return ResponseEntity.ok(rolService.obtenerTodosLosRoles());
    }

    @Operation(summary = "Crear nuevo rol", description = "Crea un nuevo rol en el sistema")
    @PostMapping
    public ResponseEntity<?> crearRol(@RequestBody RolDTO rolDTO, HttpServletRequest request) {
        // Verificar permisos
        ResponseEntity<?> authCheck = checkManageRolesPermission(request);
        if (authCheck != null) {
            return authCheck;
        }
        try {
            System.out.println("=== INICIO crearRol ===");
            System.out.println("Creando rol: " + rolDTO.getNombre());
            System.out.println("Permisos: " + rolDTO.getPermisos());
            
            // Convertir DTO a entidad
            Rol rol = new Rol();
            rol.setNombre(rolDTO.getNombre());
            rol.setDescripcion(rolDTO.getDescripcion());
            
            // Convertir permisos de String a Permiso
            if (rolDTO.getPermisos() != null) {
                Set<Permiso> permisos = new HashSet<>();
                for (String permisoNombre : rolDTO.getPermisos()) {
                    Permiso permiso = new Permiso();
                    permiso.setNombre(permisoNombre);
                    permisos.add(permiso);
                }
                rol.setPermisos(permisos);
            }
            
            Rol rolCreado = rolService.crearRol(rol);
            System.out.println("Rol creado exitosamente: " + rolCreado.getNombre());
            System.out.println("=== FIN crearRol ===");
            
            // Convertir a DTO de respuesta para evitar recursión
            RolResponseDTO responseDTO = convertirAResponseDTO(rolCreado);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            System.err.println("=== ERROR en crearRol ===");
            System.err.println("Error al crear rol: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=== FIN ERROR ===");
            
            return ResponseEntity.status(500).body(Map.of(
                "error", "Error al crear rol: " + e.getMessage()
            ));
        }
    }

    @Operation(summary = "Obtener rol por ID", description = "Devuelve un rol específico por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerRol(@PathVariable("id") Long id, HttpServletRequest request) {
        // Verificar permisos
        ResponseEntity<?> authCheck = checkManageRolesPermission(request);
        if (authCheck != null) {
            return authCheck;
        }
        try {
            System.out.println("=== INICIO obtenerRol ===");
            System.out.println("Obteniendo rol con ID: " + id);
            
            Rol rol = rolService.obtenerRol(id);
            System.out.println("Rol obtenido: " + rol.getNombre());
            System.out.println("=== FIN obtenerRol ===");
            
            // Convertir a DTO de respuesta para evitar recursión
            RolResponseDTO responseDTO = convertirAResponseDTO(rol);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            System.err.println("=== ERROR en obtenerRol ===");
            System.err.println("Error al obtener rol con ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            System.err.println("=== FIN ERROR ===");
            
            return ResponseEntity.status(500).body(Map.of(
                "error", "Error al obtener rol: " + e.getMessage(),
                "id", id
            ));
        }
    }

    @Operation(summary = "Actualizar rol", description = "Actualiza los datos de un rol existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarRol(@PathVariable("id") Long id, @RequestBody RolDTO rolDTO, HttpServletRequest request) {
        // Verificar permisos
        ResponseEntity<?> authCheck = checkManageRolesPermission(request);
        if (authCheck != null) {
            return authCheck;
        }
        try {
            System.out.println("=== INICIO actualizarRol ===");
            System.out.println("Actualizando rol con ID: " + id);
            System.out.println("Nuevos datos del rol: " + rolDTO.getNombre());
            System.out.println("Permisos: " + rolDTO.getPermisos());
            
            // Convertir DTO a entidad
            Rol rol = new Rol();
            rol.setNombre(rolDTO.getNombre());
            rol.setDescripcion(rolDTO.getDescripcion());
            
            // Convertir permisos de String a Permiso
            if (rolDTO.getPermisos() != null) {
                Set<Permiso> permisos = new HashSet<>();
                for (String permisoNombre : rolDTO.getPermisos()) {
                    Permiso permiso = new Permiso();
                    permiso.setNombre(permisoNombre);
                    permisos.add(permiso);
                }
                rol.setPermisos(permisos);
            }
            
            Rol rolActualizado = rolService.actualizarRol(id, rol);
            System.out.println("Rol actualizado exitosamente: " + rolActualizado.getNombre());
            System.out.println("=== FIN actualizarRol ===");
            
            // Convertir a DTO de respuesta para evitar recursión
            RolResponseDTO responseDTO = convertirAResponseDTO(rolActualizado);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            System.err.println("=== ERROR en actualizarRol ===");
            System.err.println("Error al actualizar rol con ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            System.err.println("=== FIN ERROR ===");
            
            return ResponseEntity.status(500).body(Map.of(
                "error", "Error al actualizar rol: " + e.getMessage(),
                "id", id
            ));
        }
    }
}
