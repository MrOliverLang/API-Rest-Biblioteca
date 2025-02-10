package com.biblioteca.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "prestamos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Relación muchos a uno: Muchos préstamos pueden corresponder a un Libro
    @JoinColumn(name = "libro_id", nullable = false) // Clave foránea en la tabla prestamos
    private Libro libro;

    @ManyToOne // Relación muchos a uno: Muchos préstamos pueden corresponder a un Usuario
    @JoinColumn(name = "usuario_id", nullable = false) // Clave foránea en la tabla prestamos
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDate fechaPrestamo;

    private LocalDate fechaDevolucion;
}