package com.tienda.dtos.marca;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "Datos necesarios para registrar una nueva marca en el sistema")
public class MarcaRequest {

    @Schema(description = "Nombre único de la marca",
            example = "L'Oréal",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;
}