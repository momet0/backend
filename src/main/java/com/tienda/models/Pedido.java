package com.tienda.models;

import com.tienda.enums.EstadoPedido;
import com.tienda.enums.MetodoPago;
import com.tienda.enums.TipoVenta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;
    private Double total;
    private String cliente;
    private String telefono;
    @Enumerated(EnumType.STRING)
    private EstadoPedido estadoPedido;
        @Enumerated(EnumType.STRING)
    private TipoVenta tipoVenta;
    private String localidad;
    private String codigoPostal;
    private String direccion;
    private String nombreBusquedaCliente;
        @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> items = new ArrayList<>();

    public void addDetalle(DetallePedido detallePedido){
        this.items.add(detallePedido);
        detallePedido.setPedido(this);
    }

    @PrePersist
    @PreUpdate
    public void generarNombreBusquedaCliente(){
        this.nombreBusquedaCliente = sinAcentos(this.getCliente());
    }

    private String sinAcentos(String nombreBusqueda){
        return Normalizer.normalize(nombreBusqueda, Normalizer.Form.NFD)
                .replaceAll("\\p{InCOMBINING_DIACRITICAL_MARKS}+","");
    }

}
