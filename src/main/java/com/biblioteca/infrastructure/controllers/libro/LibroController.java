package com.biblioteca.infrastructure.controllers.libro;

import com.biblioteca.application.service.libro.LibroServicio;
import com.biblioteca.infrastructure.controllers.libro.dto.LibroDTORequest;
import com.biblioteca.infrastructure.controllers.libro.dto.LibroDTOResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/libros")
@Tag(name = "Libros", description = "Operaciones relacionadas con la gestión de libros")
public class LibroController {

    private static final Logger logger = LoggerFactory.getLogger(LibroController.class);

    private final LibroServicio libroServicio;

    @Autowired
    public LibroController(LibroServicio libroServicio) {
        this.libroServicio = libroServicio;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los libros", description = "Retorna una lista de todos los libros disponibles en la biblioteca.")
    @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(schema = @Schema(implementation = LibroDTOResponse.class)))
    public ResponseEntity<List<LibroDTOResponse>> obtenerTodosLosLibros() {
        logger.info("Petición GET a /libros");
        List<LibroDTOResponse> libros = libroServicio.obtenerTodosLosLibros();
        return new ResponseEntity<>(libros, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un libro por ID", description = "Retorna los detalles de un libro específico buscado por su ID.")
    @ApiResponse(responseCode = "200", description = "Libro encontrado", content = @Content(schema = @Schema(implementation = LibroDTOResponse.class)))
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    public ResponseEntity<LibroDTOResponse> obtenerLibroPorId(@Parameter(in = ParameterIn.PATH, name = "id", description = "ID del libro a obtener", required = true, schema = @Schema(type = "integer", format = "int64")) @PathVariable Long id) {
        logger.info("Petición GET a /libros/{}", id);
        Optional<LibroDTOResponse> libro = libroServicio.obtenerLibroPorId(id);
        return libro.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo libro", description = "Crea un nuevo libro en la biblioteca.")
    @ApiResponse(responseCode = "201", description = "Libro creado exitosamente", content = @Content(schema = @Schema(implementation = LibroDTOResponse.class)))
    @ApiResponse(responseCode = "400", description = "Petición inválida")
    public ResponseEntity<LibroDTOResponse> crearLibro(@RequestBody(description = "Datos del libro a crear", required = true, content = @Content(schema = @Schema(implementation = LibroDTORequest.class))) LibroDTORequest libroDTORequest) {
        logger.info("Petición POST a /libros con datos: {}", libroDTORequest);
        LibroDTOResponse nuevoLibro = libroServicio.guardarLibro(libroDTORequest);
        return new ResponseEntity<>(nuevoLibro, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un libro existente", description = "Actualiza los detalles de un libro existente buscado por su ID.")
    @ApiResponse(responseCode = "200", description = "Libro actualizado exitosamente", content = @Content(schema = @Schema(implementation = LibroDTOResponse.class)))
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    @ApiResponse(responseCode = "400", description = "Petición inválida")
    public ResponseEntity<LibroDTOResponse> actualizarLibro(@Parameter(in = ParameterIn.PATH, name = "id", description = "ID del libro a actualizar", required = true, schema = @Schema(type = "integer", format = "int64")) @PathVariable Long id,
                                                            @RequestBody(description = "Datos actualizados del libro", required = true, content = @Content(schema = @Schema(implementation = LibroDTORequest.class))) LibroDTORequest libroDTORequest) {
        logger.info("Petición PUT a /libros/{} con datos: {}", id, libroDTORequest);
        LibroDTOResponse libro = libroServicio.actualizarLibro(id, libroDTORequest);
        if (libro != null) {
            return new ResponseEntity<>(libro, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un libro existente", description = "Actualiza parcialmente los detalles de un libro existente buscado por su ID.")
    @ApiResponse(responseCode = "200", description = "Libro actualizado parcialmente exitosamente", content = @Content(schema = @Schema(implementation = LibroDTOResponse.class)))
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    @ApiResponse(responseCode = "400", description = "Petición inválida")
    public ResponseEntity<LibroDTOResponse> actualizarParcialmenteLibro(@Parameter(in = ParameterIn.PATH, name = "id", description = "ID del libro a actualizar parcialmente", required = true, schema = @Schema(type = "integer", format = "int64")) @PathVariable Long id,
                                                                        @RequestBody(description = "Datos actualizados del libro", required = true, content = @Content(schema = @Schema(implementation = LibroDTORequest.class))) LibroDTORequest libroDTORequest) {
        logger.info("Petición PATCH a /libros/{} con datos: {}", id, libroDTORequest);
        LibroDTOResponse libro = libroServicio.actualizarLibro(id, libroDTORequest);
        if (libro != null) {
            return new ResponseEntity<>(libro, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un libro", description = "Elimina un libro de la biblioteca por su ID.")
    @ApiResponse(responseCode = "204", description = "Libro eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    public ResponseEntity<Void> eliminarLibro(@Parameter(in = ParameterIn.PATH, name = "id", description = "ID del libro a eliminar", required = true, schema = @Schema(type = "integer", format = "int64")) @PathVariable Long id) {
        logger.info("Petición DELETE a /libros/{}", id);
        libroServicio.eliminarLibro(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
