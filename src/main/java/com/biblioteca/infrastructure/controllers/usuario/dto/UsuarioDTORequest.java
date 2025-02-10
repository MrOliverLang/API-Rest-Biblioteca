package com.biblioteca.infrastructure.controllers.usuario.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTORequest {

    private String nombre;
    private String email;
    private String telefono;
    private LocalDate fechaRegistro;
}