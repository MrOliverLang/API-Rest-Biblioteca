package com.biblioteca.application.service.prestamo;

import com.biblioteca.domain.entity.Libro;
import com.biblioteca.domain.entity.Prestamo;
import com.biblioteca.domain.entity.Usuario;
import com.biblioteca.infrastructure.controllers.prestamo.dto.PrestamoDTORequest;
import com.biblioteca.infrastructure.controllers.prestamo.dto.PrestamoDTOResponse;
import com.biblioteca.infrastructure.controllers.prestamo.mapper.PrestamoMapper;
import com.biblioteca.infrastructure.exceptions.RecursoNoEncontradoExcepcion;
import com.biblioteca.infrastructure.repository.LibroRepositorio;
import com.biblioteca.infrastructure.repository.PrestamoRepositorio;
import com.biblioteca.infrastructure.repository.UsuarioRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrestamoServicioImpl implements PrestamoServicio {

    private static final Logger logger = LoggerFactory.getLogger(PrestamoServicioImpl.class);

    private final PrestamoRepositorio prestamoRepositorio;
    private final LibroRepositorio libroRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final PrestamoMapper prestamoMapper;

    @Autowired
    public PrestamoServicioImpl(PrestamoRepositorio prestamoRepositorio, LibroRepositorio libroRepositorio, UsuarioRepositorio usuarioRepositorio, PrestamoMapper prestamoMapper) {
        this.prestamoRepositorio = prestamoRepositorio;
        this.libroRepositorio = libroRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.prestamoMapper = prestamoMapper;
    }

    @Override
    public List<PrestamoDTOResponse> obtenerTodosLosPrestamos() {
        logger.info("Obteniendo todos los prestamos");
        List<Prestamo> prestamos = prestamoRepositorio.findAll();
        return prestamos.stream()
                .map(prestamoMapper::toDTOResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PrestamoDTOResponse> obtenerPrestamoPorId(Long id) {
        logger.info("Obteniendo prestamo por ID: {}", id);
        return Optional.ofNullable(prestamoRepositorio.findById(id)
                .map(prestamoMapper::toDTOResponse)
                .orElseThrow(() -> {
                    logger.error("Prestamo no encontrado con ID: {}", id);
                    throw new RecursoNoEncontradoExcepcion("Prestamo no encontrado con ID: " + id);
                }));
    }

    @Override
    public PrestamoDTOResponse guardarPrestamo(PrestamoDTORequest prestamoDTORequest) {
        logger.info("Guardando prestamo: {}", prestamoDTORequest);
        //Validaciones adicionales antes de guardar (ej: libro y usuario existen)
        Optional<Libro> libroOptional = libroRepositorio.findById(prestamoDTORequest.getLibroId());
        Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(prestamoDTORequest.getUsuarioId());

        if (!libroOptional.isPresent()) {
            logger.error("Libro no encontrado con ID: {}", prestamoDTORequest.getLibroId());
            throw new RecursoNoEncontradoExcepcion("Libro no encontrado con ID: " + prestamoDTORequest.getLibroId() + " para préstamo");
        }
        if (!usuarioOptional.isPresent()) {
            logger.error("Usuario no encontrado con ID: {}", prestamoDTORequest.getUsuarioId());
            throw new RecursoNoEncontradoExcepcion("Usuario no encontrado con ID: " + prestamoDTORequest.getUsuarioId() + " para préstamo");
        }

        Prestamo prestamo = prestamoMapper.toEntity(prestamoDTORequest);
        //Asignar entidades Libro y Usuario al préstamo (en lugar de solo IDs)
        prestamo.setLibro(libroOptional.get());
        prestamo.setUsuario(usuarioOptional.get());

        Prestamo prestamoGuardado = prestamoRepositorio.save(prestamo);
        return prestamoMapper.toDTOResponse(prestamoGuardado);
    }

    @Override
    public PrestamoDTOResponse actualizarPrestamo(Long id, PrestamoDTORequest prestamoDTORequest) {
        logger.info("Actualizando prestamo con ID: {} y datos: {}", id, prestamoDTORequest);
        if (prestamoDTORequest == null) {
            throw new NullPointerException("PrestamoDTORequest no puede ser nulo para actualizar");
        }
        return prestamoRepositorio.findById(id)
                .map(prestamoExistente -> {
                    //Validaciones adicionales antes de actualizar (ej: libro y usuario existen)
                    Optional<Libro> libroOptional = libroRepositorio.findById(prestamoDTORequest.getLibroId());
                    Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(prestamoDTORequest.getUsuarioId());

                    if (!libroOptional.isPresent()) {
                        logger.error("Libro no encontrado con ID: {}", prestamoDTORequest.getLibroId());
                        throw new RecursoNoEncontradoExcepcion("Libro no encontrado con ID: " + prestamoDTORequest.getLibroId() + " para préstamo");
                    }
                    if (!usuarioOptional.isPresent()) {
                        logger.error("Usuario no encontrado con ID: {}", prestamoDTORequest.getUsuarioId());
                        throw new RecursoNoEncontradoExcepcion("Usuario no encontrado con ID: " + prestamoDTORequest.getUsuarioId() + " para préstamo");
                    }

                    Prestamo prestamoActualizado = prestamoMapper.toEntity(prestamoDTORequest);
                    prestamoActualizado.setId(id);
                    //Asignar entidades Libro y Usuario al préstamo (en lugar de solo IDs)
                    prestamoActualizado.setLibro(libroOptional.get());
                    prestamoActualizado.setUsuario(usuarioOptional.get());

                    Prestamo prestamoGuardado = prestamoRepositorio.save(prestamoActualizado);
                    return prestamoMapper.toDTOResponse(prestamoGuardado);
                })
                .orElseThrow(() -> {
                    logger.error("Prestamo no encontrado para actualizar con ID: {}", id);
                    return new RecursoNoEncontradoExcepcion("Prestamo no encontrado para actualizar con ID: " + id);
                });
    }

    @Override
    public void eliminarPrestamo(Long id) {
        logger.info("Eliminando prestamo con ID: {}", id);
        if (!prestamoRepositorio.existsById(id)) {
            logger.error("Intento de eliminar prestamo no existente con ID: {}", id);
            throw new RecursoNoEncontradoExcepcion("Prestamo no encontrado para eliminar con ID: " + id);
        }
        prestamoRepositorio.deleteById(id);
    }
}