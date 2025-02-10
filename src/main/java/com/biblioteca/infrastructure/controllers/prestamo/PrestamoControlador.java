package com.biblioteca.infrastructure.controllers.prestamo;

import com.biblioteca.application.service.prestamo.PrestamoServicio;
import com.biblioteca.infrastructure.controllers.prestamo.dto.PrestamoDTORequest;
import com.biblioteca.infrastructure.controllers.prestamo.dto.PrestamoDTOResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/prestamos")
@Tag(name = "Prestamos", description = "Operaciones relacionadas con la gestión de préstamos de libros")
public class PrestamoControlador {

    private static final Logger logger = LoggerFactory.getLogger(PrestamoControlador.class);

    private final PrestamoServicio prestamoServicio;

    @Autowired
    public PrestamoControlador(PrestamoServicio prestamoServicio) {
        this.prestamoServicio = prestamoServicio;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los préstamos", description = "Retorna una lista de todos los préstamos registrados.")
    @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(schema = @Schema(implementation = PrestamoDTOResponse.class)))
    public ResponseEntity<List<PrestamoDTOResponse>> obtenerTodosLosPrestamos() {
        logger.info("Petición GET a /prestamos");
        List<PrestamoDTOResponse> prestamos = prestamoServicio.obtenerTodosLosPrestamos();
        return new ResponseEntity<>(prestamos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un préstamo por ID", description = "Retorna los detalles de un préstamo específico buscado por su ID.")
    @ApiResponse(responseCode = "200", description = "Préstamo encontrado", content = @Content(schema = @Schema(implementation = PrestamoDTOResponse.class)))
    @ApiResponse(responseCode = "404", description = "Préstamo no encontrado")
    public ResponseEntity<PrestamoDTOResponse> obtenerPrestamoPorId(@Parameter(in = ParameterIn.PATH, name = "id", description = "ID del préstamo a obtener", required = true, schema = @Schema(type = "integer", format = "int64")) @PathVariable Long id) {
        logger.info("Petición GET a /prestamos/{}", id);
        Optional<PrestamoDTOResponse> prestamo = prestamoServicio.obtenerPrestamoPorId(id);
        return prestamo.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo préstamo", description = "Registra un nuevo préstamo de un libro a un usuario.")
    @ApiResponse(responseCode = "201", description = "Préstamo creado exitosamente", content = @Content(schema = @Schema(implementation = PrestamoDTOResponse.class)))
    @ApiResponse(responseCode = "400", description = "Petición inválida")
    public ResponseEntity<PrestamoDTOResponse> crearPrestamo(@RequestBody(description = "Datos del préstamo a crear (incluye IDs de libro y usuario)", required = true, content = @Content(schema = @Schema(implementation = PrestamoDTORequest.class))) PrestamoDTORequest prestamoDTORequest) {
        logger.info("Petición POST a /prestamos con datos: {}", prestamoDTORequest);
        PrestamoDTOResponse nuevoPrestamo = prestamoServicio.guardarPrestamo(prestamoDTORequest);
        return new ResponseEntity<>(nuevoPrestamo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un préstamo existente", description = "Actualiza los detalles de un préstamo existente buscado por su ID.")
    @ApiResponse(responseCode = "200", description = "Préstamo actualizado exitosamente", content = @Content(schema = @Schema(implementation = PrestamoDTOResponse.class)))
    @ApiResponse(responseCode = "404", description = "Préstamo no encontrado")
    @ApiResponse(responseCode = "400", description = "Petición inválida")
    public ResponseEntity<PrestamoDTOResponse> actualizarPrestamo(@Parameter(in = ParameterIn.PATH, name = "id", description = "ID del préstamo a actualizar", required = true, schema = @Schema(type = "integer", format = "int64")) @PathVariable Long id,
                                                                  @RequestBody(description = "Datos actualizados del préstamo", required = true, content = @Content(schema = @Schema(implementation = PrestamoDTORequest.class))) PrestamoDTORequest prestamoDTORequest) {
        logger.info("Petición PUT a /prestamos/{} con datos: {}", id, prestamoDTORequest);
        PrestamoDTOResponse prestamo = prestamoServicio.actualizarPrestamo(id, prestamoDTORequest);
        if (prestamo != null) {
            return new ResponseEntity<>(prestamo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un préstamo existente", description = "Actualiza parcialmente los detalles de un préstamo existente buscado por su ID.")
    @ApiResponse(responseCode = "200", description = "Préstamo actualizado parcialmente exitosamente", content = @Content(schema = @Schema(implementation = PrestamoDTOResponse.class)))
    @ApiResponse(responseCode = "404", description = "Préstamo no encontrado")
    @ApiResponse(responseCode = "400", description = "Petición inválida")
    public ResponseEntity<PrestamoDTOResponse> actualizarParcialmentePrestamo(@Parameter(in = ParameterIn.PATH, name = "id", description = "ID del préstamo a actualizar parcialmente", required = true, schema = @Schema(type = "integer", format = "int64")) @PathVariable Long id,
                                                                              @RequestBody(description = "Datos actualizados del préstamo", required = true, content = @Content(schema = @Schema(implementation = PrestamoDTORequest.class))) PrestamoDTORequest prestamoDTORequest) {
        logger.info("Petición PATCH a /prestamos/{} con datos: {}", id, prestamoDTORequest);
        PrestamoDTOResponse prestamo = prestamoServicio.actualizarPrestamo(id, prestamoDTORequest); // Reutilizamos el método PUT
        if (prestamo != null) {
            return new ResponseEntity<>(prestamo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un préstamo", description = "Elimina un préstamo de la biblioteca por su ID.")
    @ApiResponse(responseCode = "204", description = "Préstamo eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Préstamo no encontrado")
    public ResponseEntity<Void> eliminarPrestamo(@Parameter(in = ParameterIn.PATH, name = "id", description = "ID del préstamo a eliminar", required = true, schema = @Schema(type = "integer", format = "int64")) @PathVariable Long id) {
        logger.info("Petición DELETE a /prestamos/{}", id);
        prestamoServicio.eliminarPrestamo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
