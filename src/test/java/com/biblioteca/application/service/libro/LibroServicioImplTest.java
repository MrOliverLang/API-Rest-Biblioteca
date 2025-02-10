package com.biblioteca.application.service.libro;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.biblioteca.domain.entity.Libro;
import com.biblioteca.infrastructure.controllers.libro.dto.LibroDTORequest;
import com.biblioteca.infrastructure.controllers.libro.dto.LibroDTOResponse;
import com.biblioteca.infrastructure.controllers.libro.mapper.LibroMapper;
import com.biblioteca.infrastructure.exceptions.RecursoNoEncontradoExcepcion;
import com.biblioteca.infrastructure.repository.LibroRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LibroServicioImplTest {

    @Mock
    private LibroRepositorio libroRepositorio;

    @Mock
    private LibroMapper libroMapper;

    @InjectMocks
    private LibroServicioImpl libroServicio;

    private Libro libro;
    private LibroDTORequest libroDTORequest;
    private LibroDTOResponse libroDTOResponse;

    @BeforeEach
    void setUp() {
        libro = new Libro();
        libro.setId(1L);
        libro.setTitulo("Título");
        libro.setAutor("Autor");
        libro.setFechaPublicacion(LocalDate.of(1967, 5, 30));

        libroDTORequest = new LibroDTORequest();
        libroDTORequest.setTitulo("Título");
        libroDTORequest.setAutor("Autor");
        libroDTORequest.setFechaPublicacion(LocalDate.of(1967, 5, 30));

        libroDTOResponse = new LibroDTOResponse();
        libroDTOResponse.setId(1L);
        libroDTOResponse.setTitulo("Título");
        libroDTOResponse.setAutor("Autor");
        libroDTOResponse.setFechaPublicacion(LocalDate.of(1967, 5, 30));
    }

    @Test
    @DisplayName("Obtener todos los libros")
    void testObtenerTodosLosLibros() {
        // Arrange
        // Create a list containing the sample book
        List<Libro> listaLibros = List.of(libro);
        when(libroRepositorio.findAll()).thenReturn(listaLibros);
        when(libroMapper.toDTOResponse(libro)).thenReturn(libroDTOResponse);

        // Act
        List<LibroDTOResponse> resultados = libroServicio.obtenerTodosLosLibros();

        // Assert
        assertNotNull(resultados, "La lista de libros no debe ser null");
        assertEquals(1, resultados.size(), "Se esperaba que la lista contenga 1 libro");
        assertEquals(libroDTOResponse, resultados.get(0), "El DTO del libro no coincide con el esperado");

        // Verifica que el repositorio y el mapper se llamaron exactamente una vez
        verify(libroRepositorio, times(1)).findAll();
        verify(libroMapper, times(1)).toDTOResponse(libro);
    }

    @Test
    @DisplayName("Obtener libro por ID - Libro Existente")
    void testObtenerLibroPorId_LibroExistente() {
        // Arrange
        when(libroRepositorio.findById(1L)).thenReturn(Optional.of(libro));
        when(libroMapper.toDTOResponse(libro)).thenReturn(libroDTOResponse);

        // Act
        Optional<LibroDTOResponse> resultado = libroServicio.obtenerLibroPorId(1L);

        // Assert
        assertTrue(resultado.isPresent(), "Se esperaba que el libro existiera");
        assertEquals(libroDTOResponse, resultado.get(), "El DTO retornado no coincide con el esperado");
        verify(libroRepositorio, times(1)).findById(1L);
        verify(libroMapper, times(1)).toDTOResponse(libro);
    }

    @Test
    @DisplayName("Obtener libro por ID - Libro No Existente")
    void testObtenerLibroPorId_LibroNoExistente() {
        // Arrange
        when(libroRepositorio.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RecursoNoEncontradoExcepcion exception = assertThrows(
                RecursoNoEncontradoExcepcion.class,
                () -> libroServicio.obtenerLibroPorId(1L),
                "Se esperaba RecursoNoEncontradoExcepcion al buscar un libro inexistente"
        );
        assertEquals("Libro no encontrado con ID: 1", exception.getMessage());
        verify(libroRepositorio, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Guardar Libro")
    void testGuardarLibro() {
        // Arrange
        when(libroMapper.toEntity(libroDTORequest)).thenReturn(libro);
        when(libroRepositorio.save(libro)).thenReturn(libro);
        when(libroMapper.toDTOResponse(libro)).thenReturn(libroDTOResponse);

        // Act
        LibroDTOResponse resultado = libroServicio.guardarLibro(libroDTORequest);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser null");
        assertEquals(libroDTOResponse, resultado, "El DTO retornado no coincide con el esperado");
        verify(libroMapper, times(1)).toEntity(libroDTORequest);
        verify(libroRepositorio, times(1)).save(libro);
        verify(libroMapper, times(1)).toDTOResponse(libro);
    }

    @Test
    @DisplayName("Actualizar Libro - Libro Existente")
    void testActualizarLibro_LibroExistente() {
        // Arrange
        when(libroRepositorio.findById(1L)).thenReturn(Optional.of(libro));
        when(libroMapper.toEntity(libroDTORequest)).thenReturn(libro);
        when(libroRepositorio.save(libro)).thenReturn(libro);
        when(libroMapper.toDTOResponse(libro)).thenReturn(libroDTOResponse);

        // Act
        LibroDTOResponse resultado = libroServicio.actualizarLibro(1L, libroDTORequest);

        // Assert
        assertNotNull(resultado, "El resultado no debería ser null");
        assertEquals(libroDTOResponse, resultado, "El DTO retornado no coincide con el esperado");
        verify(libroRepositorio, times(1)).findById(1L);
        verify(libroRepositorio, times(1)).save(libro);
        verify(libroMapper, times(1)).toDTOResponse(libro);
    }

    @Test
    @DisplayName("Actualizar Libro - Libro No Existente")
    void testActualizarLibro_LibroNoExistente() {
        // Arrange
        when(libroRepositorio.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RecursoNoEncontradoExcepcion exception = assertThrows(
                RecursoNoEncontradoExcepcion.class,
                () -> libroServicio.actualizarLibro(1L, libroDTORequest),
                "Se esperaba RecursoNoEncontradoExcepcion al actualizar un libro inexistente"
        );
        assertEquals("Libro no encontrado para actualizar con ID: 1", exception.getMessage());
        verify(libroRepositorio, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Eliminar Libro - Libro Existente")
    void testEliminarLibro_LibroExistente() {
        // Arrange
        when(libroRepositorio.existsById(1L)).thenReturn(true);

        // Act
        libroServicio.eliminarLibro(1L);

        // Assert
        verify(libroRepositorio, times(1)).existsById(1L);
        verify(libroRepositorio, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar Libro - Libro No Existente")
    void testEliminarLibro_LibroNoExistente() {
        // Arrange
        when(libroRepositorio.existsById(1L)).thenReturn(false);

        // Act & Assert
        RecursoNoEncontradoExcepcion exception = assertThrows(
                RecursoNoEncontradoExcepcion.class,
                () -> libroServicio.eliminarLibro(1L),
                "Se esperaba RecursoNoEncontradoExcepcion al eliminar un libro inexistente"
        );
        assertEquals("Libro no encontrado para eliminar con ID: 1", exception.getMessage());
        verify(libroRepositorio, times(1)).existsById(1L);
    }
}
