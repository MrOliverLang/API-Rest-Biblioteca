package com.biblioteca.infrastructure.controllers.usuario;

import com.biblioteca.application.service.usuario.UsuarioServicio;
import com.biblioteca.infrastructure.controllers.usuario.dto.UsuarioDTORequest;
import com.biblioteca.infrastructure.controllers.usuario.dto.UsuarioDTOResponse;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
public class UsuarioControlador {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class);

    private final UsuarioServicio usuarioServicio;

    @Autowired
    public UsuarioControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista de todos los usuarios registrados en la biblioteca.")
    @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(schema = @Schema(implementation = UsuarioDTOResponse.class)))
    public ResponseEntity<List<UsuarioDTOResponse>> obtenerTodosLosUsuarios() {
        logger.info("Petición GET a /usuarios");
        List<UsuarioDTOResponse> usuarios = usuarioServicio.obtenerTodosLosUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario por ID", description = "Retorna los detalles de un usuario específico buscado por su ID.")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = @Content(schema = @Schema(implementation = UsuarioDTOResponse.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<UsuarioDTOResponse> obtenerUsuarioPorId(@Parameter(in = ParameterIn.PATH, name = "id", description = "ID del usuario a obtener", required = true, schema = @Schema(type = "integer", format = "int64")) @PathVariable Long id) {
        logger.info("Petición GET a /usuarios/{}", id);
        Optional<UsuarioDTOResponse> usuario = usuarioServicio.obtenerUsuarioPorId(id);
        return usuario.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario", description = "Registra un nuevo usuario en la biblioteca.")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente", content = @Content(schema = @Schema(implementation = UsuarioDTOResponse.class)))
    @ApiResponse(responseCode = "400", description = "Petición inválida")
    public ResponseEntity<UsuarioDTOResponse> crearUsuario(@RequestBody(description = "Datos del usuario a crear", required = true, content = @Content(schema = @Schema(implementation = UsuarioDTORequest.class))) UsuarioDTORequest usuarioDTORequest) {
        logger.info("Petición POST a /usuarios con datos: {}", usuarioDTORequest);
        UsuarioDTOResponse nuevoUsuario = usuarioServicio.guardarUsuario(usuarioDTORequest);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario existente", description = "Actualiza los detalles de un usuario existente buscado por su ID.")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente", content = @Content(schema = @Schema(implementation = UsuarioDTOResponse.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "400", description = "Petición inválida")
    public ResponseEntity<UsuarioDTOResponse> actualizarUsuario(@Parameter(in = ParameterIn.PATH, name = "id", description = "ID del usuario a actualizar", required = true, schema = @Schema(type = "integer", format = "int64")) @PathVariable Long id,
                                                                @RequestBody(description = "Datos actualizados del usuario", required = true, content = @Content(schema = @Schema(implementation = UsuarioDTORequest.class))) UsuarioDTORequest usuarioDTORequest) {
        logger.info("Petición PUT a /usuarios/{} con datos: {}", id, usuarioDTORequest);
        UsuarioDTOResponse usuario = usuarioServicio.actualizarUsuario(id, usuarioDTORequest);
        if (usuario != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un usuario existente", description = "Actualiza parcialmente los detalles de un usuario existente buscado por su ID.")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado parcialmente exitosamente", content = @Content(schema = @Schema(implementation = UsuarioDTOResponse.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "400", description = "Petición inválida")
    public ResponseEntity<UsuarioDTOResponse> actualizarParcialmenteUsuario(@Parameter(in = ParameterIn.PATH, name = "id", description = "ID del usuario a actualizar parcialmente", required = true, schema = @Schema(type = "integer", format = "int64")) @PathVariable Long id,
                                                                            @RequestBody(description = "Datos actualizados del usuario", required = true, content = @Content(schema = @Schema(implementation = UsuarioDTORequest.class))) UsuarioDTORequest usuarioDTORequest) {
        logger.info("Petición PATCH a /usuarios/{} con datos: {}", id, usuarioDTORequest);
        UsuarioDTOResponse usuario = usuarioServicio.actualizarUsuario(id, usuarioDTORequest); // Reutilizamos el método PUT
        if (usuario != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario de la biblioteca por su ID.")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<Void> eliminarUsuario(@Parameter(in = ParameterIn.PATH, name = "id", description = "ID del usuario a eliminar", required = true, schema = @Schema(type = "integer", format = "int64")) @PathVariable Long id) {
        logger.info("Petición DELETE a /usuarios/{}", id);
        usuarioServicio.eliminarUsuario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
