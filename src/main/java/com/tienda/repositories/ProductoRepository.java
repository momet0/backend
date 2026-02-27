package com.tienda.repositories;

import com.tienda.enums.EstadoPedido;
import com.tienda.models.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Long>, JpaSpecificationExecutor<Producto> {
        List<Producto> findByCategoriaId(Long categoriaId);
        @Query("SELECT COUNT(p) FROM Producto p WHERE p.stock <= p.minStock AND p.activo = true")
        long countProductosConStockBajo();
        long countByStockAndActivoTrue(int stock);
        long countByActivoTrue();
        long countByActivoFalse();
        @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.categoria LEFT JOIN FETCH p.proveedor WHERE p.id = :id")
        Optional<Producto> findByIdWithCategoriaAndProveedor(@Param("id") Long id);

        Optional<Producto> findByCodigoBarra(String codigoBarra);

        List<Producto> findByMarcaIdAndActivoTrue(Long marcaId);

        List<Producto> findByProveedorIdAndActivoTrue(Long proveedorId);
}
