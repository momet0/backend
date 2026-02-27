package com.tienda.dtos.pedido;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Resumen estadístico de pedidos por estado")
public class PedidoResumenDTO {

    @Schema(description = "Total de pedidos registrados",
            example = "1245")
    private long total;

    @Schema(description = "Pedidos en estado EN_PREPARACION",
            example = "120")
    private long en_preparacion;

    @Schema(description = "Pedidos en estado PENDIENTE",
            example = "85")
    private long pendientes;

    @Schema(description = "Pedidos en estado ENTREGADO",
            example = "950")
    private long entregados;

    @Schema(description = "Pedidos en estado CANCELADO",
            example = "90")
    private long cancelados;
}
