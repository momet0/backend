package com.tienda.dtos.pedido;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tienda.enums.EstadoPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Respuesta que representa un pedido realizado por un cliente. Incluye información general del pedido, datos del cliente, estado y el detalle de productos asociados")
public class PedidoResponse extends PedidoBaseDTO {
    @Schema(description = "Identificador único", example = "15")
    private Long id;

    @Schema(description = "Fecha y hora de creación", example = "2024-01-24T10:30:00")
    private LocalDateTime fecha;

    @Schema(description = "Monto total calculado", example = "12500.50")
    private Double total;

    @Schema(description = "Estado actual del flujo de venta", example = "PENDIENTE")
    private EstadoPedido estado;

    @Schema(description = "Listado de productos incluidos en el pedido")
    private List<DetalleResponse> items;

    @Data
    @Schema(description = "Detalle de un producto incluido dentro de un pedido")
    public static class DetalleResponse {

        @Schema(description = "Identificador único del detalle del pedido",
                example = "1")
        private Long id;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Schema(description = "En caso de ser produicto simple devuelve el link de la img, caso contrario no recibe ese campo")
        private String imagenUrl;

        @Schema(description = "Nombre del producto al momento de realizar el pedido",
                example = "Shampoo 900ml")
        private String descripcion;

        @Schema(description = "Precio unitario del producto en el momento de la compra",
                example = "1800.00")
        private Double precioUnitario;

        @Schema(description = "Cantidad solicitada del producto",
                example = "2")
        private int cantidad;

        @Schema(description = "Subtotal calculado para este producto (precio x cantidad)",
                example = "3600.00")
        private Double precioFinal;

        @Schema(description = "Tipo de item, puede ser promocion o producto")
        private String tipo;

        @Schema(description = "en caso de ser una promocion se mostrará una lista de los productos que la componen")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<ProductosIncluidos> productos;

        @Data
        public static class ProductosIncluidos{
            private Long id;
            private String nombre;
            private Integer cantidad;
        }
    }
}
