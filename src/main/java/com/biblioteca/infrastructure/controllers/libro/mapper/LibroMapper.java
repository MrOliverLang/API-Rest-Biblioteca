package com.biblioteca.infrastructure.controllers.libro.mapper;

import com.biblioteca.domain.entity.Libro;
import com.biblioteca.infrastructure.controllers.libro.dto.LibroDTORequest;
import com.biblioteca.infrastructure.controllers.libro.dto.LibroDTOResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LibroMapper {

    LibroDTORequest toDTORequest(Libro libro);
    LibroDTOResponse toDTOResponse(Libro libro);
    Libro toEntity(LibroDTORequest libroDTORequest);
    Libro toEntity(LibroDTOResponse libroDTOResponse);
}
