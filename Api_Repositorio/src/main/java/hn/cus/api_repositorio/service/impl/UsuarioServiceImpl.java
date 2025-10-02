/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.cus.api_repositorio.service.impl;

/**
 *
 * @author EG490082
 */
import hn.cus.api_repositorio.dto.UsuarioDTO;
import hn.cus.api_repositorio.entity.Usuario;
import hn.cus.api_repositorio.entity.Rol;
import hn.cus.api_repositorio.repository.UsuarioRepository;
import hn.cus.api_repositorio.repository.RolRepository;
import hn.cus.api_repositorio.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public UsuarioDTO obtenerUsuarioDTO(Long id) {
        try {
            System.out.println("=== INICIO obtenerUsuarioDTO ===");
            System.out.println("Buscando usuario con ID: " + id);
            
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
            
            System.out.println("Usuario encontrado: " + usuario.getUsername());
            System.out.println("Roles del usuario: " + (usuario.getRoles() != null ? usuario.getRoles().size() : "null"));
            
            List<String> rolesList = new ArrayList<>();
            if (usuario.getRoles() != null) {
                for (Rol rol : usuario.getRoles()) {
                    rolesList.add(rol.getNombre());
                    System.out.println("Rol encontrado: " + rol.getNombre());
                }
            }
            
            UsuarioDTO dto = new UsuarioDTO(
                    usuario.getId(),
                    usuario.getUsername(),
                    usuario.getEmail(),
                    null, // No devolver password
                    usuario.getActivo(),
                    rolesList
            );
            
            System.out.println("DTO creado exitosamente");
            System.out.println("=== FIN obtenerUsuarioDTO ===");
            return dto;
            
        } catch (Exception e) {
            System.err.println("=== ERROR en obtenerUsuarioDTO ===");
            System.err.println("Error al obtener usuario con ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            System.err.println("=== FIN ERROR ===");
            throw new RuntimeException("Error al obtener usuario: " + e.getMessage());
        }
    }

    @Override
    public List<UsuarioDTO> obtenerTodosUsuarios() {
        try {
            return usuarioRepository.findAll().stream()
                    .map(usuario -> new UsuarioDTO(
                            usuario.getId(),
                            usuario.getUsername(),
                            usuario.getEmail(),
                            null, // No devolver password
                            usuario.getActivo(),
                            usuario.getRoles() != null ? 
                                usuario.getRoles().stream()
                                        .map(rol -> rol.getNombre())
                                        .collect(Collectors.toList()) : 
                                new ArrayList<>()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error al obtener todos los usuarios: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al obtener usuarios: " + e.getMessage());
        }
    }

    @Override
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        usuario.setActivo(usuarioDTO.getActivo());
        
        // Asignar roles
        if (usuarioDTO.getRoles() != null && !usuarioDTO.getRoles().isEmpty()) {
            Set<Rol> roles = new HashSet<>();
            for (String roleName : usuarioDTO.getRoles()) {
                rolRepository.findByNombre(roleName).ifPresent(roles::add);
            }
            usuario.setRoles(roles);
        }
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return new UsuarioDTO(
                usuarioGuardado.getId(),
                usuarioGuardado.getUsername(),
                usuarioGuardado.getEmail(),
                null, // No devolver password
                usuarioGuardado.getActivo(),
                usuarioGuardado.getRoles().stream()
                        .map(Rol::getNombre)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setActivo(usuarioDTO.getActivo());
        
        // Actualizar contraseña si se proporciona
        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        }
        
        // Actualizar roles
        if (usuarioDTO.getRoles() != null) {
            Set<Rol> roles = new HashSet<>();
            for (String roleName : usuarioDTO.getRoles()) {
                rolRepository.findByNombre(roleName).ifPresent(roles::add);
            }
            usuario.setRoles(roles);
        }
        
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return new UsuarioDTO(
                usuarioActualizado.getId(),
                usuarioActualizado.getUsername(),
                usuarioActualizado.getEmail(),
                null, // No devolver password
                usuarioActualizado.getActivo(),
                usuarioActualizado.getRoles().stream()
                        .map(Rol::getNombre)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
