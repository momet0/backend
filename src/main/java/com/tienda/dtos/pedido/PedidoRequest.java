package com.tienda.dtos.pedido;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Datos necesarios para registrar un pedido")
public class PedidoRequest extends PedidoBaseDTO {

    @NotEmpty(message = "El pedido debe contener al menos un ítem")
    @Valid // Valida cada detalle del pedido
    @Schema(description = "Lista de productos o promociones incluidos")
    private List<DetalleRequest> productos;

    @Data
    @Schema(description = "Detalle de producto dentro del pedido")
    public static class DetalleRequest {

        @Schema(description = "ID del producto (si no se usa promoción)",
                example = "5")
        private Long productId;

        @Schema(description = "Cantidad solicitada del producto",
                example = "3",
                requiredMode = Schema.RequiredMode.REQUIRED)
        private Integer cantidad;

        @Schema(description = "ID de la promoción (si no se usa producto individual)",
                example = "2")
        private Long promocionId;
    }
}
