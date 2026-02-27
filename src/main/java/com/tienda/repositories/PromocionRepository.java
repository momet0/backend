package com.tienda.repositories;

import com.tienda.models.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PromocionRepository extends JpaRepository<Promocion, Long>, JpaSpecificationExecutor<Promocion> {
    Promocion findByIdAndActivoTrue(Long id);
    List<Promocion> findByProductosDetalleProductoIdAndActivoTrue(Long id);
}
