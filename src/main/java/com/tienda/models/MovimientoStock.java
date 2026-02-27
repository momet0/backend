package com.tienda.models;

import com.tienda.enums.TipoCambio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import com.tienda.enums.MotivoMovimiento;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movimiento_stock")
public class MovimientoStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @NotNull(message = "debe existir un valor numérico")
    private Integer valorNumerico;

    @Column(name = "valor_anterior")
    private Double valorAnterior;

    @Column(name = "valor_actual")
    private Double valorActual;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TipoCambio tipoCambio;

    @Enumerated(EnumType.STRING)
    private MotivoMovimiento motivo;

    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "producto_id")   
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "marca_id")
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
}
