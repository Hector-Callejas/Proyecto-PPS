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
import hn.cus.api_repositorio.repository.UsuarioRepository;
import hn.cus.api_repositorio.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired private UsuarioRepository usuarioRepository;

    @Override
    public UsuarioDTO obtenerUsuarioDTO(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getRoles().stream()
                        .map(rol -> rol.getNombre())
                        .collect(Collectors.toList())
        );
    }
}
