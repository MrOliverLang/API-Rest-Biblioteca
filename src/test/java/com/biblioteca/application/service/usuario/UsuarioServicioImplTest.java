package com.biblioteca.application.service.usuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.biblioteca.domain.entity.Usuario;
import com.biblioteca.infrastructure.controllers.usuario.dto.UsuarioDTORequest;
import com.biblioteca.infrastructure.controllers.usuario.dto.UsuarioDTOResponse;
import com.biblioteca.infrastructure.controllers.usuario.mapper.UsuarioMapper;
import com.biblioteca.infrastructure.exceptions.RecursoNoEncontradoExcepcion;
import com.biblioteca.infrastructure.repository.UsuarioRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UsuarioServicioImplTest {

    @Mock
    private UsuarioRepositorio usuarioRepositorio;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioServicioImpl usuarioServicio;

    private Usuario usuario;
    private UsuarioDTORequest usuarioDTORequest;
    private UsuarioDTOResponse usuarioDTOResponse;

    @BeforeEach
    void setUp() {
        // Inicializando los valores del usuario
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setEmail("juan@example.com");
        usuario.setTelefono("123456789");
        usuario.setFechaRegistro(LocalDate.of(2020, 5, 20));

        // Inicializando los datos para la petición
        usuarioDTORequest = new UsuarioDTORequest();
        usuarioDTORequest.setNombre("Juan");
        usuarioDTORequest.setEmail("juan@example.com");
        usuarioDTORequest.setTelefono("123456789");
        usuarioDTORequest.setFechaRegistro(LocalDate.of(2020, 5, 20));

        // Inicializando los datos de la respuesta
        usuarioDTOResponse = new UsuarioDTOResponse();
        usuarioDTOResponse.setId(1L);
        usuarioDTOResponse.setNombre("Juan");
        usuarioDTOResponse.setEmail("juan@example.com");
        usuarioDTOResponse.setTelefono("123456789");
        usuarioDTOResponse.setFechaRegistro(LocalDate.of(2020, 5, 20));
    }

    @Test
    @DisplayName("Obtener todos los usuarios")
    void testObtenerTodosLosUsuarios() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepositorio.findAll()).thenReturn(usuarios);
        when(usuarioMapper.toDTOResponse(usuario)).thenReturn(usuarioDTOResponse);

        // Act
        List<UsuarioDTOResponse> resultado = usuarioServicio.obtenerTodosLosUsuarios();

        // Assert
        assertNotNull(resultado, "El resultado no debería ser null");
        assertFalse(resultado.isEmpty(), "La lista de usuarios no debería estar vacía");
        assertEquals(1, resultado.size(), "Se esperaba una lista con 1 usuario");
        assertEquals(usuarioDTOResponse, resultado.get(0), "El DTO retornado no coincide con el esperado");
        verify(usuarioRepositorio, times(1)).findAll();
        verify(usuarioMapper, times(1)).toDTOResponse(usuario);
    }


    @Test
    @DisplayName("Obtener usuario por ID - Usuario Existente")
    void testObtenerUsuarioPorId_UsuarioExistente() {
        // Arrange
        when(usuarioRepositorio.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toDTOResponse(usuario)).thenReturn(usuarioDTOResponse);

        // Act
        Optional<UsuarioDTOResponse> resultado = usuarioServicio.obtenerUsuarioPorId(1L);

        // Assert
        assertTrue(resultado.isPresent(), "Se esperaba que el usuario existiera");
        assertEquals(usuarioDTOResponse, resultado.get(), "El DTO retornado no coincide con el esperado");
        verify(usuarioRepositorio, times(1)).findById(1L);
        verify(usuarioMapper, times(1)).toDTOResponse(usuario);
    }

    @Test
    @DisplayName("Obtener usuario por ID - Usuario No Existente")
    void testObtenerUsuarioPorId_UsuarioNoExistente() {
        // Arrange
        when(usuarioRepositorio.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RecursoNoEncontradoExcepcion exception = assertThrows(
                RecursoNoEncontradoExcepcion.class,
                () -> usuarioServicio.obtenerUsuarioPorId(1L),
                "Se esperaba RecursoNoEncontradoExcepcion al buscar un usuario inexistente"
        );
        assertEquals("Usuario no encontrado con ID: 1", exception.getMessage());
        verify(usuarioRepositorio, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Guardar Usuario")
    void testGuardarUsuario() {
        // Arrange
        when(usuarioMapper.toEntity(usuarioDTORequest)).thenReturn(usuario);
        when(usuarioRepositorio.save(usuario)).thenReturn(usuario);
        when(usuarioMapper.toDTOResponse(usuario)).thenReturn(usuarioDTOResponse);

        // Act
        UsuarioDTOResponse resultado = usuarioServicio.guardarUsuario(usuarioDTORequest);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser null");
        assertEquals(usuarioDTOResponse, resultado, "El DTO retornado no coincide con el esperado");
        verify(usuarioMapper, times(1)).toEntity(usuarioDTORequest);
        verify(usuarioRepositorio, times(1)).save(usuario);
        verify(usuarioMapper, times(1)).toDTOResponse(usuario);
    }

    @Test
    @DisplayName("Actualizar Usuario - Usuario Existente")
    void testActualizarUsuario_UsuarioExistente() {
        // Arrange
        when(usuarioRepositorio.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toEntity(usuarioDTORequest)).thenReturn(usuario);
        when(usuarioRepositorio.save(usuario)).thenReturn(usuario);
        when(usuarioMapper.toDTOResponse(usuario)).thenReturn(usuarioDTOResponse);

        // Act
        UsuarioDTOResponse resultado = usuarioServicio.actualizarUsuario(1L, usuarioDTORequest);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser null");
        assertEquals(usuarioDTOResponse, resultado, "El DTO retornado no coincide con el esperado");
        verify(usuarioRepositorio, times(1)).findById(1L);
        verify(usuarioRepositorio, times(1)).save(usuario);
        verify(usuarioMapper, times(1)).toDTOResponse(usuario);
    }

    @Test
    @DisplayName("Actualizar Usuario - Usuario No Existente")
    void testActualizarUsuario_UsuarioNoExistente() {
        // Arrange
        when(usuarioRepositorio.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RecursoNoEncontradoExcepcion exception = assertThrows(
                RecursoNoEncontradoExcepcion.class,
                () -> usuarioServicio.actualizarUsuario(1L, usuarioDTORequest),
                "Se esperaba RecursoNoEncontradoExcepcion al actualizar un usuario inexistente"
        );
        assertEquals("Usuario no encontrado para actualizar con ID: 1", exception.getMessage());
        verify(usuarioRepositorio, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Eliminar Usuario - Usuario Existente")
    void testEliminarUsuario_UsuarioExistente() {
        // Arrange
        when(usuarioRepositorio.existsById(1L)).thenReturn(true);

        // Act
        usuarioServicio.eliminarUsuario(1L);

        // Assert
        verify(usuarioRepositorio, times(1)).existsById(1L);
        verify(usuarioRepositorio, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar Usuario - Usuario No Existente")
    void testEliminarUsuario_UsuarioNoExistente() {
        // Arrange
        when(usuarioRepositorio.existsById(1L)).thenReturn(false);

        // Act & Assert
        RecursoNoEncontradoExcepcion exception = assertThrows(
                RecursoNoEncontradoExcepcion.class,
                () -> usuarioServicio.eliminarUsuario(1L),
                "Se esperaba RecursoNoEncontradoExcepcion al eliminar un usuario inexistente"
        );
        assertEquals("Usuario no encontrado para eliminar con ID: 1", exception.getMessage());
        verify(usuarioRepositorio, times(1)).existsById(1L);
    }
}
