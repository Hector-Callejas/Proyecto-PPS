package hn.cus.api_repositorio.service;

import hn.cus.api_repositorio.entity.Usuario;
import hn.cus.api_repositorio.entity.Rol;
import hn.cus.api_repositorio.entity.Permiso;
import hn.cus.api_repositorio.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.HashSet;

/**
 * Servicio para manejar la autorización basada en roles y permisos
 */
@Service
public class AuthorizationService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Verifica si un usuario tiene un permiso específico
     * @param username - Nombre de usuario
     * @param permissionName - Nombre del permiso
     * @return true si el usuario tiene el permiso
     */
    public boolean hasPermission(String username, String permissionName) {
        try {
            Usuario usuario = usuarioRepository.findByUsername(username)
                    .orElse(null);
            
            if (usuario == null || usuario.getRoles() == null) {
                return false;
            }

            // Verificar si algún rol del usuario tiene el permiso
            for (Rol rol : usuario.getRoles()) {
                if (rol.getPermisos() != null) {
                    for (Permiso permiso : rol.getPermisos()) {
                        if (permiso.getNombre().equals(permissionName)) {
                            return true;
                        }
                    }
                }
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("Error verificando permiso: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si un usuario tiene un rol específico
     * @param username - Nombre de usuario
     * @param roleName - Nombre del rol
     * @return true si el usuario tiene el rol
     */
    public boolean hasRole(String username, String roleName) {
        try {
            Usuario usuario = usuarioRepository.findByUsername(username)
                    .orElse(null);
            
            if (usuario == null || usuario.getRoles() == null) {
                return false;
            }

            for (Rol rol : usuario.getRoles()) {
                if (rol.getNombre().equals(roleName)) {
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("Error verificando rol: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los permisos de un usuario
     * @param username - Nombre de usuario
     * @return Set de nombres de permisos
     */
    public Set<String> getUserPermissions(String username) {
        Set<String> permissions = new HashSet<>();
        
        try {
            Usuario usuario = usuarioRepository.findByUsername(username)
                    .orElse(null);
            
            if (usuario != null && usuario.getRoles() != null) {
                for (Rol rol : usuario.getRoles()) {
                    if (rol.getPermisos() != null) {
                        for (Permiso permiso : rol.getPermisos()) {
                            permissions.add(permiso.getNombre());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error obteniendo permisos: " + e.getMessage());
        }
        
        return permissions;
    }

    /**
     * Verifica si un usuario puede gestionar roles (tiene permiso MANAGE_ROLES)
     * @param username - Nombre de usuario
     * @return true si puede gestionar roles
     */
    public boolean canManageRoles(String username) {
        return hasPermission(username, "MANAGE_ROLES");
    }

    /**
     * Verifica si un usuario puede gestionar usuarios (tiene permiso MANAGE_USERS)
     * @param username - Nombre de usuario
     * @return true si puede gestionar usuarios
     */
    public boolean canManageUsers(String username) {
        return hasPermission(username, "MANAGE_USERS");
    }

    /**
     * Verifica si un usuario puede subir documentos (tiene permiso UPLOAD_DOCUMENT)
     * @param username - Nombre de usuario
     * @return true si puede subir documentos
     */
    public boolean canUploadDocuments(String username) {
        return hasPermission(username, "UPLOAD_DOCUMENT");
    }
}
