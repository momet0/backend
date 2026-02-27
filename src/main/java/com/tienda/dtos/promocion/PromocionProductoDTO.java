package com.tienda.dtos.promocion;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Información básica de una promoción asociada a un producto")
public class PromocionProductoDTO {

    @Schema(description = "Identificador único de la promoción generado por el sistema",
            example = "101")
    private Long id;

    @Schema(description = "Nombre comercial de la promoción",
            example = "Combo Coloración Profesional")
    private String nombre;
}
