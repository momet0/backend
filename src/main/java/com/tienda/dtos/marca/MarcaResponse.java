package com.tienda.dtos.marca;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "Información detallada de una marca registrada")
public class MarcaResponse {

    @Schema(description = "Identificador único autogenerado",
            example = "1")
    private Long id;

    @Schema(description = "Nombre comercial de la marca",
            example = "L'Oréal")
    private String nombre;
}
