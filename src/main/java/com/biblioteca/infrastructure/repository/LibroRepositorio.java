package com.biblioteca.infrastructure.repository;

import com.biblioteca.domain.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Opcional, pero buena práctica indicar que es un repositorio
public interface LibroRepositorio extends JpaRepository<Libro, Long> {
    // Aquí podemos añadir métodos personalizados para consultas específicas si fueran necesarias
}
