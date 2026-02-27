package com.tienda.dtos.promocion;

import com.tienda.dtos.producto.ProductoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "Información detallada de un producto dentro de una promoción")
public class PromocionItemResponse {

    @Schema(description = "Datos completos del producto")
    private ProductoDTO producto;

    @Schema(description = "Cantidad incluida en la promoción",
            example = "3")
    private Integer cantidad;

}
