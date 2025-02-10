package com.biblioteca.application.service.prestamo;

import com.biblioteca.infrastructure.controllers.prestamo.dto.PrestamoDTORequest;
import com.biblioteca.infrastructure.controllers.prestamo.dto.PrestamoDTOResponse;

import java.util.List;
import java.util.Optional;

public interface PrestamoServicio {
    List<PrestamoDTOResponse> obtenerTodosLosPrestamos();

    Optional<PrestamoDTOResponse> obtenerPrestamoPorId(Long id);

    PrestamoDTOResponse guardarPrestamo(PrestamoDTORequest prestamoDTORequest);

    PrestamoDTOResponse actualizarPrestamo(Long id, PrestamoDTORequest prestamoDTORequest);

    void eliminarPrestamo(Long id);
}
