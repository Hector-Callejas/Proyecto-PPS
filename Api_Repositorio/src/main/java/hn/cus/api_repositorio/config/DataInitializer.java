package hn.cus.api_repositorio.config;

import hn.cus.api_repositorio.entity.Rol;
import hn.cus.api_repositorio.entity.Usuario;
import hn.cus.api_repositorio.entity.Permiso;
import hn.cus.api_repositorio.repository.RolRepository;
import hn.cus.api_repositorio.repository.UsuarioRepository;
import hn.cus.api_repositorio.repository.PermisoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private RolRepository rolRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PermisoRepository permisoRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    @Transactional // ✅ Un solo persistence context: evita detached
    public void run(String... args) throws Exception {

        // 1) Crear permisos si no existen (idempotente)
        Map<String, String> seedPerms = Map.of(
            "UPLOAD_DOCUMENT",   "Subir documentos",
            "VIEW_DOCUMENT",     "Ver documentos",
            "DOWNLOAD_DOCUMENT", "Descargar documentos",
            "DELETE_DOCUMENT",   "Eliminar documentos",
            "MANAGE_USERS",      "Gestionar usuarios",
            "MANAGE_ROLES",      "Gestionar roles",
            "VIEW_AUDIT",        "Ver auditoría",
            "MANAGE_WORKFLOW",   "Gestionar workflow"
        );

        seedPerms.forEach((nombre, desc) ->
            permisoRepository.findByNombre(nombre)
                .orElseGet(() -> permisoRepository.save(new Permiso(null, nombre, desc)))
        );

        // Recuperar TODOS los permisos como entidades MANAGED
        List<Permiso> allPerms = permisoRepository.findAll();
        Map<String, Permiso> byName = allPerms.stream()
            .collect(Collectors.toMap(Permiso::getNombre, p -> p));

        // 2) Roles: crear solo si no existen, asociando permisos MANAGED
        if (rolRepository.count() == 0) {

            // ADMIN con todos los permisos
            Rol adminRole = new Rol();
            adminRole.setNombre("ADMIN");
            adminRole.setDescripcion("Administrador del sistema");
            adminRole.setPermisos(new HashSet<>(allPerms));
            rolRepository.save(adminRole);

            // USER con permisos básicos
            Rol userRole = new Rol();
            userRole.setNombre("USER");
            userRole.setDescripcion("Usuario básico");
            userRole.setPermisos(new HashSet<>(Arrays.asList(
                mustGet(byName, "VIEW_DOCUMENT"),
                mustGet(byName, "DOWNLOAD_DOCUMENT")
            )));
            rolRepository.save(userRole);

            // User3 con permisos de documentos
            Rol user3Role = new Rol();
            user3Role.setNombre("User3");
            user3Role.setDescripcion("Usuarios de nivel 3");
            user3Role.setPermisos(new HashSet<>(Arrays.asList(
                mustGet(byName, "UPLOAD_DOCUMENT"),
                mustGet(byName, "VIEW_DOCUMENT"),
                mustGet(byName, "DOWNLOAD_DOCUMENT"),
                mustGet(byName, "DELETE_DOCUMENT")
            )));
            rolRepository.save(user3Role);
        }

        // 3) Usuarios: crear si no existen, vinculando roles MANAGED
        if (usuarioRepository.count() == 0) {
            Rol adminRole = rolRepository.findByNombre("ADMIN").orElseThrow();
            Rol userRole  = rolRepository.findByNombre("USER").orElseThrow();

            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@gestor.com");
            admin.setActivo(true);
            admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
            usuarioRepository.save(admin);

            Usuario user = new Usuario();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setEmail("user@gestor.com");
            user.setActivo(true);
            user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
            usuarioRepository.save(user);
        }
    }

    private static Permiso mustGet(Map<String, Permiso> byName, String key) {
        Permiso p = byName.get(key);
        if (p == null) throw new IllegalStateException("Permiso no encontrado: " + key);
        return p;
    }
}
