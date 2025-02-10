package com.biblioteca.infrastructure.controllers.libro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroDTORequest {

    private String titulo;
    private String autor;
    private String isbn;
    private LocalDate fechaPublicacion;
}
