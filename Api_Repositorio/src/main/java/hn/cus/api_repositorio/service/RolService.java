package hn.cus.api_repositorio.service;

import hn.cus.api_repositorio.entity.Rol;
import java.util.List;

/**
 *
 * @author EG490082
 */
public interface RolService {
    List<Rol> obtenerTodosLosRoles();
    Rol obtenerRol(Long id);
    Rol crearRol(Rol rol);
    Rol actualizarRol(Long id, Rol rol);
}
