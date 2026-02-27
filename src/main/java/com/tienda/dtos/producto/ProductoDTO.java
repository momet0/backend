package com.tienda.dtos.producto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo que representa la información simple de un producto")
public class ProductoDTO extends ProductoBaseDTO {

    @Schema(description = "Nombre de la categoría", example = "Maquillaje")
    private String categoria;

    @Schema(description = "Nombre del proveedor", example = "L'Oréal Argentina SA")
    private String proveedor;

    @Schema(description = "Nombre de la marca", example = "Plusbell")
    private String marca;

}
