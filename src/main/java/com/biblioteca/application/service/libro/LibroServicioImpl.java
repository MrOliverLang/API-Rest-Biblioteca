package com.biblioteca.application.service.libro;

import com.biblioteca.domain.entity.Libro;
import com.biblioteca.infrastructure.controllers.libro.dto.LibroDTORequest;
import com.biblioteca.infrastructure.controllers.libro.dto.LibroDTOResponse;
import com.biblioteca.infrastructure.controllers.libro.mapper.LibroMapper;
import com.biblioteca.infrastructure.exceptions.RecursoNoEncontradoExcepcion;
import com.biblioteca.infrastructure.repository.LibroRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class LibroServicioImpl implements LibroServicio {

    private static final Logger logger = LoggerFactory.getLogger(LibroServicioImpl.class);

    private final LibroRepositorio libroRepositorio;
    private final LibroMapper libroMapper;

    @Autowired
    public LibroServicioImpl(LibroRepositorio libroRepositorio, LibroMapper libroMapper) {
        this.libroRepositorio = libroRepositorio;
        this.libroMapper = libroMapper;
    }

    @Override
    public List<LibroDTOResponse> obtenerTodosLosLibros() {
        logger.info("Obteniendo todos los libros");
        List<Libro> libros = libroRepositorio.findAll();
        return libros.stream()
                .map(libroMapper::toDTOResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LibroDTOResponse> obtenerLibroPorId(Long id) {
        logger.info("Obteniendo libro por ID: {}", id);
        return libroRepositorio.findById(id)
                .map(libroMapper::toDTOResponse)
                .or(() -> {
                    logger.error("Libro no encontrado con ID: {}", id);
                    throw new RecursoNoEncontradoExcepcion("Libro no encontrado con ID: " + id);
                });
    }

    @Override
    public LibroDTOResponse guardarLibro(LibroDTORequest libroDTORequest) {
        logger.info("Guardando libro: {}", libroDTORequest);
        Libro libro = libroMapper.toEntity(libroDTORequest);
        Libro libroGuardado = libroRepositorio.save(libro);
        return libroMapper.toDTOResponse(libroGuardado);
    }

    @Override
    public LibroDTOResponse actualizarLibro(Long id, LibroDTORequest libroDTORequest) {
        logger.info("Actualizando libro con ID: {} y datos: {}", id, libroDTORequest);
        return libroRepositorio.findById(id)
                .map(libroExistente -> {
                    Libro libroActualizado = libroMapper.toEntity(libroDTORequest);
                    libroActualizado.setId(id);
                    Libro libroGuardado = libroRepositorio.save(libroActualizado);
                    return libroMapper.toDTOResponse(libroGuardado);
                })
                .orElseThrow(() -> {
                    logger.error("Libro no encontrado para actualizar con ID: {}", id);
                    return new RecursoNoEncontradoExcepcion("Libro no encontrado para actualizar con ID: " + id);
                });
    }

    @Override
    public void eliminarLibro(Long id) {
        logger.info("Eliminando libro con ID: {}", id);
        if (!libroRepositorio.existsById(id)) {
            logger.error("Intento de eliminar libro no existente con ID: {}", id);
            throw new RecursoNoEncontradoExcepcion("Libro no encontrado para eliminar con ID: " + id);
        }
        libroRepositorio.deleteById(id);
    }
}