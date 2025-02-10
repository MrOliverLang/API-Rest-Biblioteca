package com.biblioteca.application.service.libro;

import com.biblioteca.domain.entity.Libro;
import com.biblioteca.infrastructure.controllers.libro.dto.LibroDTORequest;
import com.biblioteca.infrastructure.controllers.libro.dto.LibroDTOResponse;

import java.util.List;
import java.util.Optional;

public interface LibroServicio {
    List<LibroDTOResponse> obtenerTodosLosLibros();
    Optional<LibroDTOResponse> obtenerLibroPorId(Long id);
    LibroDTOResponse guardarLibro(LibroDTORequest libro);
    LibroDTOResponse actualizarLibro(Long id, LibroDTORequest libro);
    void eliminarLibro(Long id);
}
