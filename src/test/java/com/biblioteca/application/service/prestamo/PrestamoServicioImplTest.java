package com.biblioteca.application.service.prestamo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unitarios para PrestamoServicioImpl")
public class PrestamoServicioImplTest {

    @Mock
    private PrestamoRepositorio prestamoRepositorio;

    @Mock
    private LibroRepositorio libroRepositorio;

    @Mock
    private UsuarioRepositorio usuarioRepositorio;

    @Mock
    private PrestamoMapper prestamoMapper;

    @InjectMocks
    private PrestamoServicioImpl prestamoServicio;

    private Prestamo prestamo;
    private PrestamoDTORequest prestamoDTORequest;
    private PrestamoDTOResponse prestamoDTOResponse;
    private Libro libro;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Inicializando los valores para cada test
        libro = new Libro();
        libro.setId(1L);
        libro.setTitulo("El Quijote");
        libro.setAutor("Miguel de Cervantes");
        libro.setFechaPublicacion(LocalDate.of(1605, 1, 1));
        usuario = new Usuario(1L, "Juan", "juan@example.com", "123456789", LocalDate.of(2020, 5, 20));

        prestamo = new Prestamo();
        prestamo.setId(1L);
        prestamo.setLibro(libro);
        prestamo.setUsuario(usuario);
        prestamo.setFechaPrestamo(LocalDate.of(2023, 1, 1));
        prestamo.setFechaDevolucion(LocalDate.of(2023, 2, 1));

        // DTO para request
        prestamoDTORequest = new PrestamoDTORequest();
        prestamoDTORequest.setLibroId(libro.getId());
        prestamoDTORequest.setUsuarioId(usuario.getId());
        prestamoDTORequest.setFechaPrestamo(LocalDate.of(2023, 1, 1));
        prestamoDTORequest.setFechaDevolucion(LocalDate.of(2023, 2, 1));

        // DTO para response
        prestamoDTOResponse = new PrestamoDTOResponse();
        prestamoDTOResponse.setId(1L);
        prestamoDTOResponse.setLibroId(libro.getId());
        prestamoDTOResponse.setUsuarioId(usuario.getId());
        prestamoDTOResponse.setFechaPrestamo(LocalDate.of(2023, 1, 1));
        prestamoDTOResponse.setFechaDevolucion(LocalDate.of(2023, 2, 1));
    }

    @Test
    @DisplayName("Obtener todos los prestamos - Éxito")
    void testObtenerTodosLosPrestamos() {
        // Arrange
        List<Prestamo> prestamos = Arrays.asList(prestamo);
        when(prestamoRepositorio.findAll()).thenReturn(prestamos);
        when(prestamoMapper.toDTOResponse(prestamo)).thenReturn(prestamoDTOResponse);

        // Act
        List<PrestamoDTOResponse> resultado = prestamoServicio.obtenerTodosLosPrestamos();

        // Assert
        assertNotNull(resultado, "El resultado no debería ser null");
        assertFalse(resultado.isEmpty(), "La lista de préstamos no debería estar vacía");
        assertEquals(1, resultado.size(), "Se esperaba una lista con 1 préstamo");
        assertEquals(prestamoDTOResponse, resultado.get(0), "El DTO retornado no coincide con el esperado");
        verify(prestamoRepositorio, times(1)).findAll();
        verify(prestamoMapper, times(1)).toDTOResponse(prestamo);
    }

    @Test
    @DisplayName("Obtener prestamo por ID - Prestamo Existente - Éxito")
    void testObtenerPrestamoPorId_PrestamoExistente() {
        // Arrange
        when(prestamoRepositorio.findById(1L)).thenReturn(Optional.of(prestamo));
        when(prestamoMapper.toDTOResponse(prestamo)).thenReturn(prestamoDTOResponse);

        // Act
        Optional<PrestamoDTOResponse> resultado = prestamoServicio.obtenerPrestamoPorId(1L);

        // Assert
        assertTrue(resultado.isPresent(), "Se esperaba que el préstamo existiera");
        assertEquals(prestamoDTOResponse, resultado.get(), "El DTO retornado no coincide con el esperado");
        verify(prestamoRepositorio, times(1)).findById(1L);
        verify(prestamoMapper, times(1)).toDTOResponse(prestamo);
    }

    @Test
    @DisplayName("Obtener prestamo por ID - Prestamo No Existente - Lanza RecursoNoEncontradoExcepcion")
    void testObtenerPrestamoPorId_PrestamoNoExistente() {
        // Arrange
        when(prestamoRepositorio.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RecursoNoEncontradoExcepcion exception = assertThrows(
                RecursoNoEncontradoExcepcion.class,
                () -> prestamoServicio.obtenerPrestamoPorId(1L),
                "Se esperaba RecursoNoEncontradoExcepcion al buscar un préstamo inexistente"
        );
        assertEquals("Prestamo no encontrado con ID: 1", exception.getMessage());
        verify(prestamoRepositorio, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Guardar Prestamo - Libro y Usuario Existentes - Éxito")
    void testGuardarPrestamo() {
        // Arrange
        when(libroRepositorio.findById(libro.getId())).thenReturn(Optional.of(libro));
        when(usuarioRepositorio.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(prestamoMapper.toEntity(prestamoDTORequest)).thenReturn(prestamo);
        when(prestamoRepositorio.save(prestamo)).thenReturn(prestamo);
        when(prestamoMapper.toDTOResponse(prestamo)).thenReturn(prestamoDTOResponse);

        // Act
        PrestamoDTOResponse resultado = prestamoServicio.guardarPrestamo(prestamoDTORequest);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser null");
        assertEquals(prestamoDTOResponse, resultado, "El DTO retornado no coincide con el esperado");
        verify(libroRepositorio, times(1)).findById(libro.getId());
        verify(usuarioRepositorio, times(1)).findById(usuario.getId());
        verify(prestamoRepositorio, times(1)).save(prestamo);
        verify(prestamoMapper, times(1)).toDTOResponse(prestamo);
    }

    @Test
    @DisplayName("Guardar Prestamo - Libro No Existente - Lanza RecursoNoEncontradoExcepcion")
    void testGuardarPrestamo_LibroNoEncontrado() {
        // Arrange
        when(libroRepositorio.findById(libro.getId())).thenReturn(Optional.empty());

        // Act & Assert
        RecursoNoEncontradoExcepcion exception = assertThrows(
                RecursoNoEncontradoExcepcion.class,
                () -> prestamoServicio.guardarPrestamo(prestamoDTORequest),
                "Se esperaba RecursoNoEncontradoExcepcion por libro inexistente"
        );
        assertEquals("Libro no encontrado con ID: 1 para préstamo", exception.getMessage());
        verify(libroRepositorio, times(1)).findById(libro.getId());
    }


    @Test
    @DisplayName("Guardar Prestamo - Usuario No Existente - Lanza RecursoNoEncontradoExcepcion")
    public void testGuardarPrestamo_UsuarioNoEncontrado() {
        // Arrange
        when(libroRepositorio.findById(anyLong())).thenReturn(Optional.of(libro));
        when(usuarioRepositorio.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RecursoNoEncontradoExcepcion.class, () -> {
            prestamoServicio.guardarPrestamo(prestamoDTORequest);
        });

        assertEquals("Usuario no encontrado con ID: 1 para préstamo", exception.getMessage());
    }

    @Test
    @DisplayName("Guardar Prestamo - DTO Request Nulo - Lanza NullPointerException")
    public void testGuardarPrestamo_DTORequestNulo() {
        // Act & Assert
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> prestamoServicio.guardarPrestamo(null),
                "Se esperaba NullPointerException al guardar con DTO nulo"
        );
        // No es necesario verificar el mensaje, solo que se lanza la excepción esperada
    }


    @Test
    @DisplayName("Actualizar Prestamo - Prestamo Existente - Éxito")
    void testActualizarPrestamo_PrestamoExistente() {
        // Arrange
        when(prestamoRepositorio.findById(1L)).thenReturn(Optional.of(prestamo));
        when(libroRepositorio.findById(libro.getId())).thenReturn(Optional.of(libro));
        when(usuarioRepositorio.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(prestamoMapper.toEntity(prestamoDTORequest)).thenReturn(prestamo);
        when(prestamoRepositorio.save(prestamo)).thenReturn(prestamo);
        when(prestamoMapper.toDTOResponse(prestamo)).thenReturn(prestamoDTOResponse);

        // Act
        PrestamoDTOResponse resultado = prestamoServicio.actualizarPrestamo(1L, prestamoDTORequest);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser null");
        assertEquals(prestamoDTOResponse, resultado, "El DTO retornado no coincide con el esperado");
        verify(prestamoRepositorio, times(1)).findById(1L);
        verify(libroRepositorio, times(1)).findById(libro.getId());
        verify(usuarioRepositorio, times(1)).findById(usuario.getId());
        verify(prestamoRepositorio, times(1)).save(prestamo);
        verify(prestamoMapper, times(1)).toDTOResponse(prestamo);
    }

    @Test
    @DisplayName("Actualizar Prestamo - Prestamo No Encontrado - Lanza RecursoNoEncontradoExcepcion")
    public void testActualizarPrestamo_PrestamoNoEncontrado() {
        // Arrange
        when(prestamoRepositorio.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RecursoNoEncontradoExcepcion exception = assertThrows(
                RecursoNoEncontradoExcepcion.class, () -> {
                    prestamoServicio.actualizarPrestamo(1L, prestamoDTORequest);
                });

        assertEquals("Prestamo no encontrado para actualizar con ID: 1", exception.getMessage());
    }

    @Test
    @DisplayName("Actualizar Prestamo - Libro No Encontrado - Lanza RecursoNoEncontradoExcepcion")
    public void testActualizarPrestamo_LibroNoEncontrado() {
        // Arrange
        when(prestamoRepositorio.findById(anyLong())).thenReturn(Optional.of(prestamo));
        when(libroRepositorio.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RecursoNoEncontradoExcepcion exception = assertThrows(
                RecursoNoEncontradoExcepcion.class, () -> {
                    prestamoServicio.actualizarPrestamo(1L, prestamoDTORequest);
                });

        assertEquals("Libro no encontrado con ID: 1 para préstamo", exception.getMessage());
    }

    @Test
    @DisplayName("Actualizar Prestamo - Usuario No Encontrado - Lanza RecursoNoEncontradoExcepcion")
    public void testActualizarPrestamo_UsuarioNoEncontrado() {
        // Arrange
        when(prestamoRepositorio.findById(anyLong())).thenReturn(Optional.of(prestamo));
        when(libroRepositorio.findById(anyLong())).thenReturn(Optional.of(libro));
        when(usuarioRepositorio.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RecursoNoEncontradoExcepcion exception = assertThrows(
                RecursoNoEncontradoExcepcion.class, () -> {
                    prestamoServicio.actualizarPrestamo(1L, prestamoDTORequest);
                });

        assertEquals("Usuario no encontrado con ID: 1 para préstamo", exception.getMessage());
    }

    @Test
    @DisplayName("Actualizar Prestamo - DTO Request Nulo - Lanza NullPointerException")
    public void testActualizarPrestamo_DTORequestNulo() {
        // Act & Assert
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> prestamoServicio.actualizarPrestamo(1L, null),
                "Se esperaba NullPointerException al actualizar con DTO nulo"
        );
        // No es necesario verificar el mensaje, solo que se lanza la excepción esperada
    }


    @Test
    @DisplayName("Eliminar Prestamo - Prestamo Existente - Éxito")
    void testEliminarPrestamo() {
        // Arrange
        when(prestamoRepositorio.existsById(1L)).thenReturn(true);

        // Act
        prestamoServicio.eliminarPrestamo(1L);

        // Assert
        verify(prestamoRepositorio, times(1)).existsById(1L);
        verify(prestamoRepositorio, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar Prestamo - Prestamo No Existente - Lanza RecursoNoEncontradoExcepcion")
    void testEliminarPrestamo_NoExistente() {
        // Arrange
        when(prestamoRepositorio.existsById(1L)).thenReturn(false);

        // Act & Assert
        RecursoNoEncontradoExcepcion exception = assertThrows(
                RecursoNoEncontradoExcepcion.class,
                () -> prestamoServicio.eliminarPrestamo(1L),
                "Se esperaba RecursoNoEncontradoExcepcion al eliminar un préstamo inexistente"
        );
        assertEquals("Prestamo no encontrado para eliminar con ID: 1", exception.getMessage());
        verify(prestamoRepositorio, times(1)).existsById(1L);
    }
}