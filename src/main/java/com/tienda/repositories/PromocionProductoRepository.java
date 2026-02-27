package com.tienda.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromocionProductoRepository extends JpaRepository<com.tienda.models.PromocionProducto, Long> {
}
