package com.biblioteca.infrastructure.controllers.prestamo.mapper;

import com.biblioteca.domain.entity.Prestamo;
import com.biblioteca.infrastructure.controllers.prestamo.dto.PrestamoDTORequest;
import com.biblioteca.infrastructure.controllers.prestamo.dto.PrestamoDTOResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrestamoMapper {

    PrestamoDTORequest toDTORequest(Prestamo prestamo);

    @Mapping(source = "libro.id", target = "libroId") // **Añade esta línea: Mapea libro.id a libroId**
    @Mapping(source = "usuario.id", target = "usuarioId") // **Añade esta línea: Mapea usuario.id a usuarioId**
    PrestamoDTOResponse toDTOResponse(Prestamo prestamo);

    Prestamo toEntity(PrestamoDTORequest prestamoDTORequest);
    Prestamo toEntity(PrestamoDTOResponse prestamoDTOResponse);
}
