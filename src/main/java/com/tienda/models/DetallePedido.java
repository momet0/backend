package com.tienda.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int cantidad;
    private Double precioUnitario;
    private Double subTotal;
    @ManyToOne
    private Producto producto;
    @ManyToOne
    private Pedido pedido;
    @ManyToOne
    @JoinColumn(name = "promocion_id")
    private Promocion promocion;
}
