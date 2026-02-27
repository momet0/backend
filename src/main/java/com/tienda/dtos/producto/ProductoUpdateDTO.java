package com.tienda.dtos.producto;


import com.tienda.dtos.CategoriaDTO;
import com.tienda.dtos.ProveedorDTO;
import com.tienda.dtos.marca.MarcaResponse;
import lombok.Data;

@Data
public class ProductoUpdateDTO {

    private Integer stock;
    private Boolean activo;
    private String nombre;
    private String codigoBarra;
    private CategoriaDTO categoria;
    private MarcaResponse marca;
    private ProveedorDTO proveedor;
    private String tono;
    private String linea;
    private String tamaño;
    private Double precio;
    private Integer minStock;

}
