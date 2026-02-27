package com.tienda.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de transferencia que representa una categoría de productos. Se utiliza para crear, actualizar y consultar categorías dentro del sistema")
public class CategoriaDTO {

    @Schema(description = "Identificador único de la categoría. Es generado automáticamente por el sistema",
            example = "5")
    private Long id;

    @Schema(description = "Nombre de la categoría. Debe ser único y descriptivo",
            example = "Maquillaje",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;
}