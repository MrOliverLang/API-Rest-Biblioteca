package com.biblioteca.infrastructure.controllers.prestamo.mapper;

import com.biblioteca.domain.entity.Prestamo;
import com.biblioteca.infrastructure.controllers.prestamo.dto.PrestamoDTORequest;
import com.biblioteca.infrastructure.controllers.prestamo.dto.PrestamoDTOResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrestamoMapper {

    PrestamoDTORequest toDTORequest(Prestamo prestamo);
    PrestamoDTOResponse toDTOResponse(Prestamo prestamo);
    Prestamo toEntity(PrestamoDTORequest prestamoDTORequest);
    Prestamo toEntity(PrestamoDTOResponse prestamoDTOResponse);
}
