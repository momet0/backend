package com.tienda.repositories.specs;


import com.tienda.enums.ActividadProducto;
import com.tienda.enums.EstadoStock;
import com.tienda.models.Marca;
import com.tienda.models.Producto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class ProductoSpecs {


    public static Specification<Producto> porFiltros(ActividadProducto actividadProducto, String nombreBusqueda, Long categoria, Long proveedor,
                                                     EstadoStock estadoStock, String marca) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nombreBusqueda != null && !nombreBusqueda.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("nombreBusqueda")), "%" + nombreBusqueda.toLowerCase() + "%"));
            }

            if(actividadProducto != null){
                if(actividadProducto == ActividadProducto.ACTIVO){
                    predicates.add(cb.equal(root.get("activo"), true));
                }else if(actividadProducto == ActividadProducto.INACTIVO){
                    predicates.add(cb.equal(root.get("activo"), false));
                }
            }

            if(estadoStock != null){
                if(estadoStock == EstadoStock.BAJO) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("stock"), root.get("minStock")));
                    predicates.add(cb.equal(root.get("activo"), true));
                }else if(estadoStock == EstadoStock.ALTO){
                    predicates.add(cb.greaterThan(root.get("stock"), root.get("minStock")));
                    predicates.add(cb.equal(root.get("activo"), true));
                }
            }

            if(categoria != null){
                predicates.add(cb.equal(root.get("categoria").get("id"),categoria));
            }

            if(proveedor != null){
                predicates.add(cb.equal(root.get("proveedor").get("id"),proveedor));
            }

            if (marca != null && !marca.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.join("marca").get("nombre")),
                        "%" + marca.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
