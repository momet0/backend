package com.tienda.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "No se admite nombre vacío")
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    private Double precio;

    @NotNull(message = "debe existir stock")
    private int stock;

    @NotNull(message = "debe existir un minimo de stock")
    private int minStock;
    private String imagenUrl;
    private boolean activo;
    private String nombreBusqueda;
    private String linea;
    private String tono;
    private String tamaño;
    private String codigoBarra;
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_proveedor")
    private Proveedor proveedor;

    @OneToMany(mappedBy = "producto")
    private List<PromocionProducto> promociones = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id")
    private Marca marca;

    @PrePersist
    @PreUpdate
    public void generarNombreBusqueda(){
        this.nombreBusqueda = sinAcentos(this.nombre);
    }

    private String sinAcentos(String nombreBusqueda){
        return Normalizer.normalize(nombreBusqueda, Normalizer.Form.NFD)
                .replaceAll("\\p{InCOMBINING_DIACRITICAL_MARKS}+","");
    }
}
