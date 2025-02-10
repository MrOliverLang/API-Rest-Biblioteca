-- Insertar Libros
INSERT INTO libros (titulo, autor, isbn, fecha_publicacion) VALUES
('Cien años de soledad', 'Gabriel García Márquez', '978-0307474728', '1967-05-30'),
('1984', 'George Orwell', '978-0451524935', '1949-06-08'),
('Un mundo feliz', 'Aldous Huxley', '978-0060850524', '1932-01-01');

-- Insertar Usuarios
INSERT INTO usuarios (nombre, email, telefono, fecha_registro) VALUES
('Alice Wonderland', 'alice@example.com', '111-222-3333', '2024-01-26'),
('Bob Builder', 'bob@example.com', '444-555-6666', '2024-01-26'),
('Charlie Chaplin', 'charlie@example.com', '777-888-9999', '2024-01-26');

-- Insertar Prestamos (asegúrate de que los IDs de libro y usuario existan en las tablas correspondientes)
INSERT INTO prestamos (libro_id, usuario_id, fecha_prestamo, fecha_devolucion) VALUES
(1, 1, '2024-01-26', '2024-02-26'), -- Préstamo de "Cien años de soledad" por Alice
(2, 2, '2024-01-20', '2024-02-20'), -- Préstamo de "1984" por Bob, devuelto anticipadamente
(3, 3, '2024-01-15', '2024-02-15'); -- Préstamo de "Un mundo feliz" por Charlie, aún no devuelto