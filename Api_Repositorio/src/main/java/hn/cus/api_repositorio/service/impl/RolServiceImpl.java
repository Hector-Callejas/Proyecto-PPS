package hn.cus.api_repositorio.service.impl;

import hn.cus.api_repositorio.entity.Permiso;
import hn.cus.api_repositorio.entity.Rol;
import hn.cus.api_repositorio.repository.PermisoRepository;
import hn.cus.api_repositorio.repository.RolRepository;
import hn.cus.api_repositorio.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author EG490082
 */
@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private PermisoRepository permisoRepository;

    @Override
    public List<Rol> obtenerTodosLosRoles() {
        return rolRepository.findAll();
    }

    @Override
    public Rol obtenerRol(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }

    @Override
    public Rol crearRol(Rol rol) {
        // Procesar permisos si se proporcionan
        if (rol.getPermisos() != null && !rol.getPermisos().isEmpty()) {
            Set<Permiso> permisosProcesados = new HashSet<>();
            
            for (Permiso permiso : rol.getPermisos()) {
                // Buscar el permiso por nombre o crear uno nuevo si no existe
                Permiso permisoExistente = permisoRepository.findByNombre(permiso.getNombre())
                        .orElseGet(() -> {
                            Permiso nuevoPermiso = new Permiso();
                            nuevoPermiso.setNombre(permiso.getNombre());
                            return permisoRepository.save(nuevoPermiso);
                        });
                permisosProcesados.add(permisoExistente);
            }
            
            rol.setPermisos(permisosProcesados);
        }
        
        return rolRepository.save(rol);
    }

    @Override
    @Transactional
    public Rol actualizarRol(Long id, Rol rol) {
        System.out.println("=== INICIO actualizarRol ===");
        System.out.println("Actualizando rol ID: " + id);
        
        try {
            Rol rolExistente = rolRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
            
            // Actualizar campos básicos
            rolExistente.setNombre(rol.getNombre());
            rolExistente.setDescripcion(rol.getDescripcion());
            
            // Guardar primero el rol básico
            rolExistente = rolRepository.save(rolExistente);
            System.out.println("Rol básico guardado: " + rolExistente.getNombre());
            
            // Actualizar permisos de forma simplificada
            if (rol.getPermisos() != null && !rol.getPermisos().isEmpty()) {
                System.out.println("Procesando permisos: " + rol.getPermisos().size());
                
                // Limpiar permisos existentes
                rolExistente.getPermisos().clear();
                rolRepository.save(rolExistente); // Guardar sin permisos primero
                
                // Procesar cada permiso
                for (Permiso permiso : rol.getPermisos()) {
                    try {
                        System.out.println("Procesando permiso: " + permiso.getNombre());
                        
                        // Buscar permiso existente
                        Permiso permisoExistente = permisoRepository.findByNombre(permiso.getNombre()).orElse(null);
                        
                        if (permisoExistente == null) {
                            // Crear nuevo permiso
                            permisoExistente = new Permiso();
                            permisoExistente.setNombre(permiso.getNombre());
                            permisoExistente = permisoRepository.save(permisoExistente);
                            System.out.println("Permiso creado: " + permisoExistente.getNombre());
                        } else {
                            System.out.println("Permiso existente encontrado: " + permisoExistente.getNombre());
                        }
                        
                        rolExistente.getPermisos().add(permisoExistente);
                    } catch (Exception e) {
                        System.err.println("Error procesando permiso: " + permiso.getNombre() + " - " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                
                // Guardar con permisos
                rolExistente = rolRepository.save(rolExistente);
                System.out.println("Rol guardado con permisos");
            }
            
            System.out.println("=== FIN actualizarRol ===");
            return rolExistente;
            
        } catch (Exception e) {
            System.err.println("=== ERROR en actualizarRol ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
