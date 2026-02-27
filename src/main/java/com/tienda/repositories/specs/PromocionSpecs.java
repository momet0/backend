package com.tienda.repositories.specs;

import com.tienda.enums.TipoPromocion;
import com.tienda.models.Promocion;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PromocionSpecs {

    public static Specification<Promocion> porFiltros(Boolean activo, TipoPromocion tipo, String nombreBusqueda) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(activo != null) {
                if (activo) {
                    predicates.add(cb.equal(root.get("activo"), true));
                }else{
                    predicates.add(cb.equal(root.get("activo"), false));
                }
            }

            if (nombreBusqueda != null && !nombreBusqueda.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("nombreBusqueda")), "%" + nombreBusqueda.toLowerCase() + "%"));
            }

            if (tipo != null){
                predicates.add(cb.equal(root.get("tipoPromocion"),tipo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
