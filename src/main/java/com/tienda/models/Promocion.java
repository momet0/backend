package com.tienda.models;

import com.tienda.enums.TipoPromocion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor


@Entity
@Table(name = "promocion")
public class Promocion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String nombreBusqueda;
    @Enumerated(EnumType.STRING)
    private TipoPromocion tipoPromocion;
    private Integer cantidadMinima;
    private Double precioFinal;
    private Boolean activo;

    @OneToMany(mappedBy = "promocion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromocionProducto> productosDetalle = new ArrayList<>();

    @OneToMany(mappedBy = "promocion")
    private List<DetallePedido> detalles = new ArrayList<>();

    @PrePersist
    @PreUpdate
    public void generarNombreBusqueda(){
        this.nombreBusqueda = sinAcentos(this.getNombre());
    }

    private String sinAcentos(String nombreBusqueda){
        return Normalizer.normalize(nombreBusqueda, Normalizer.Form.NFD)
                .replaceAll("\\p{InCOMBINING_DIACRITICAL_MARKS}+","");
    }
}
