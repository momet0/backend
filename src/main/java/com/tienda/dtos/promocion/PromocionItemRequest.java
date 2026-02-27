package com.tienda.dtos.promocion;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "Detalle de un producto incluido en una solicitud de promoción")
public class PromocionItemRequest {

    @Schema(description = "ID del producto a incluir",
            example = "5",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productoId;

    @Schema(description = "Cantidad de unidades requeridas para la promoción",
            example = "3",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidad;
}
