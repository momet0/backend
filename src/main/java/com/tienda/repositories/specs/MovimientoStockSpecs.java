package com.tienda.repositories.specs;

import com.tienda.enums.MotivoMovimiento;
import com.tienda.enums.TipoCambio;
import com.tienda.models.MovimientoStock;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimientoStockSpecs {

    public static Specification<MovimientoStock> porFiltros(TipoCambio tipoCambio, MotivoMovimiento motivo, Long productoId, Long marcaId, Long proveedorId, LocalDate inicio,
                                                            LocalDate fin, String nombreProducto, String nombreUsuario) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (tipoCambio != null) {
                predicates.add(cb.equal(root.get("tipoCambio"), tipoCambio));
            }

            if(motivo != null){
                predicates.add(cb.equal(root.get("motivo"),motivo));
            }

            if(productoId != null){ predicates.add(cb.equal(root.get("producto").get("id"),productoId)); }

            if (marcaId != null) {
                predicates.add(cb.equal(root.get("marca").get("id"), marcaId));
            }

            if (proveedorId != null) {
                predicates.add(cb.equal(root.get("proveedor").get("id"), proveedorId));
            }

            if (nombreProducto != null && !nombreProducto.isEmpty()) {
                    predicates.add(cb.like(cb.lower(root.get("producto").get("nombreBusqueda")),
                            "%" + nombreProducto.toLowerCase() + "%"
                ));
            }

            if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
                predicates.add(cb.like(
                        root.get("user").get("usernameBusqueda"),
                        "%" + nombreUsuario + "%"
                ));
            }

            if (inicio != null) {
                LocalDateTime iniciodia = inicio.atStartOfDay();
                predicates.add(cb.greaterThanOrEqualTo(root.get("fecha"), iniciodia));
            }

            if (fin != null) {
                LocalDateTime findia = fin.atTime(23, 59, 59);
                predicates.add(cb.lessThanOrEqualTo(root.get("fecha"), findia));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
