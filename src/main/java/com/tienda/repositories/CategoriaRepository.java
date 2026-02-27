package com.tienda.repositories;

import com.tienda.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tienda.models.Producto;
import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);
}
