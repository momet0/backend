package com.tienda.repositories;

import com.tienda.models.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);
    List<Proveedor> findByListaProductosNombreContainingIgnoreCase(String nombreProducto);
    List<Proveedor> findDistinctByListaProductosNombreIn(Collection<String> nombresProductos);
}
