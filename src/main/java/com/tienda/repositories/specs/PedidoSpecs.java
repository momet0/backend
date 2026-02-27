package com.tienda.repositories.specs;

import com.tienda.enums.EstadoPedido;
import com.tienda.enums.TipoVenta;
import com.tienda.models.Pedido;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoSpecs {

    public static Specification<Pedido> porFiltros(String nombreCliente, EstadoPedido estado,
                                                   LocalDate inicio, LocalDate fin, TipoVenta tipoVenta) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nombreCliente != null && !nombreCliente.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("nombreBusquedaCliente")), "%" + nombreCliente.toLowerCase() + "%"));
            }

            if (estado != null) {
                predicates.add(cb.equal(root.get("estadoPedido"), estado));
            }

            if(tipoVenta != null){
                predicates.add(cb.equal(root.get("tipoVenta"),tipoVenta));
            }

            if (inicio != null && fin != null) {
                predicates.add(cb.between(root.get("fecha"), inicio, fin));
            }

            if(inicio != null){
                LocalDateTime iniciodia = inicio.atStartOfDay();
                predicates.add(cb.greaterThanOrEqualTo(root.get("fecha"),iniciodia));
            }

            if(fin != null){
                LocalDateTime findia = fin.atTime(23,59,59);
                predicates.add(cb.lessThanOrEqualTo(root.get("fecha"),findia));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
