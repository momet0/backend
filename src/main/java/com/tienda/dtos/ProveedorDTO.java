package com.tienda.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Proveedor que suministra productos al sistema")
public class ProveedorDTO {

    @Schema(description = "Identificador único del proveedor",
            example = "2")
    private Long id;

    @Schema(description = "Nombre del proveedor",
            example = "L'Oréal Argentina SA",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;
}
