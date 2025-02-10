package com.biblioteca.application.service.usuario;

import com.biblioteca.domain.entity.Usuario;
import com.biblioteca.infrastructure.controllers.usuario.dto.UsuarioDTORequest;
import com.biblioteca.infrastructure.controllers.usuario.dto.UsuarioDTOResponse;
import com.biblioteca.infrastructure.controllers.usuario.mapper.UsuarioMapper;
import com.biblioteca.infrastructure.exceptions.RecursoNoEncontradoExcepcion;
import com.biblioteca.infrastructure.repository.UsuarioRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServicioImpl.class);

    private final UsuarioRepositorio usuarioRepositorio;
    private final UsuarioMapper usuarioMapper;

    @Autowired
    public UsuarioServicioImpl(UsuarioRepositorio usuarioRepositorio, UsuarioMapper usuarioMapper) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public List<UsuarioDTOResponse> obtenerTodosLosUsuarios() {
        logger.info("Obteniendo todos los usuarios");
        List<Usuario> usuarios = usuarioRepositorio.findAll();
        return usuarios.stream()
                .map(usuarioMapper::toDTOResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UsuarioDTOResponse> obtenerUsuarioPorId(Long id) {
        logger.info("Obteniendo usuario por ID: {}", id);
        return usuarioRepositorio.findById(id)
                .map(usuarioMapper::toDTOResponse)
                .or(() -> {
                    logger.error("Usuario no encontrado con ID: {}", id);
                    throw new RecursoNoEncontradoExcepcion("Usuario no encontrado con ID: " + id);
                });
    }

    @Override
    public UsuarioDTOResponse guardarUsuario(UsuarioDTORequest usuarioDTORequest) {
        logger.info("Guardando usuario: {}", usuarioDTORequest);
        Usuario usuario = usuarioMapper.toEntity(usuarioDTORequest);
        Usuario usuarioGuardado = usuarioRepositorio.save(usuario);
        return usuarioMapper.toDTOResponse(usuarioGuardado);
    }

    @Override
    public UsuarioDTOResponse actualizarUsuario(Long id, UsuarioDTORequest usuarioDTORequest) {
        logger.info("Actualizando usuario con ID: {} y datos: {}", id, usuarioDTORequest);
        return usuarioRepositorio.findById(id)
                .map(usuarioExistente -> {
                    Usuario usuarioActualizado = usuarioMapper.toEntity(usuarioDTORequest);
                    usuarioActualizado.setId(id);
                    Usuario usuarioGuardado = usuarioRepositorio.save(usuarioActualizado);
                    return usuarioMapper.toDTOResponse(usuarioGuardado);
                })
                .orElseThrow(() -> {
                    logger.error("Usuario no encontrado para actualizar con ID: {}", id);
                    return new RecursoNoEncontradoExcepcion("Usuario no encontrado para actualizar con ID: " + id);
                });
    }

    @Override
    public void eliminarUsuario(Long id) {
        logger.info("Eliminando usuario con ID: {}", id);
        if (!usuarioRepositorio.existsById(id)) {
            logger.error("Intento de eliminar usuario no existente con ID: {}", id);
            throw new RecursoNoEncontradoExcepcion("Usuario no encontrado para eliminar con ID: " + id);
        }
        usuarioRepositorio.deleteById(id);
    }
}
