package com.biblioteca.application.service.usuario;

import com.biblioteca.infrastructure.controllers.usuario.dto.UsuarioDTORequest;
import com.biblioteca.infrastructure.controllers.usuario.dto.UsuarioDTOResponse;

import java.util.List;
import java.util.Optional;

public interface UsuarioServicio {
    List<UsuarioDTOResponse> obtenerTodosLosUsuarios();

    Optional<UsuarioDTOResponse> obtenerUsuarioPorId(Long id);

    UsuarioDTOResponse guardarUsuario(UsuarioDTORequest usuarioDTORequest);

    UsuarioDTOResponse actualizarUsuario(Long id, UsuarioDTORequest usuarioDTORequest);

    void eliminarUsuario(Long id);
}
