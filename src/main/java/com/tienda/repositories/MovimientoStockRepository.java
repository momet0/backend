package com.tienda.repositories;

import com.tienda.enums.MotivoMovimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tienda.models.MovimientoStock;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long>, JpaSpecificationExecutor<MovimientoStock> {
    List<MovimientoStock> findByProductoIdOrderByFechaDesc(Long id);
}
