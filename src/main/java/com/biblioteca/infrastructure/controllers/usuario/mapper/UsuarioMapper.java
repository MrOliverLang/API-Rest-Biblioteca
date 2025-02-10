package com.biblioteca.infrastructure.controllers.usuario.mapper;

import com.biblioteca.infrastructure.controllers.usuario.dto.UsuarioDTORequest;
import com.biblioteca.domain.entity.Usuario;
import com.biblioteca.infrastructure.controllers.usuario.dto.UsuarioDTOResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTORequest toDTORequest(Usuario usuario);
    UsuarioDTOResponse toDTOResponse(Usuario usuario);
    Usuario toEntity(UsuarioDTORequest usuarioDTORequest);
    Usuario toEntity(UsuarioDTOResponse usuarioDTOResponse);
}
