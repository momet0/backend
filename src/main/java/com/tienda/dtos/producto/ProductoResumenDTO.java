package com.tienda.dtos.producto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Resumen estadístico del estado de stock de productos")
public class ProductoResumenDTO {

    @Schema(description = "Total de productos registrados en el sistema",
            example = "350")
    private long total;

    @Schema(description = "Productos con stock igual a cero",
            example = "27")
    private long sinStock;

    @Schema(description = "Productos con stock por debajo del mínimo establecido",
            example = "54")
    private long stockBajo;

    @Schema(description = "Productos que estan inactivos",
            example = "54")
    private long inactivos;

    @Schema(description = "Productos que estan activos",
            example = "54")
    private long activos;
}

