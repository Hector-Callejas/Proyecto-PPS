/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hn.cus.api_repositorio.service;

/**
 *
 * @author EG490082
 */
// UsuarioService.java

import hn.cus.api_repositorio.dto.UsuarioDTO;
import java.util.List;

public interface UsuarioService {
    UsuarioDTO obtenerUsuarioDTO(Long id);
    List<UsuarioDTO> obtenerTodosUsuarios();
    UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO);
    UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO);
    void eliminarUsuario(Long id);
}
