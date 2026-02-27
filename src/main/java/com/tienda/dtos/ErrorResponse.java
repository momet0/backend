package com.tienda.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Estructura estándar de respuesta para errores de la API")
public class ErrorResponse {

    @Schema(description = "Fecha y hora en la que ocurrió el error",
            example = "2026-01-22T14:35:00")
    private LocalDateTime fecha;

    @Schema(description = "Código de estado HTTP",
            example = "404")
    private int status;

    @Schema(description = "Tipo de error HTTP",
            example = "Not Found")
    private String error;

    @Schema(description = "Mensaje descriptivo del error",
            example = "Producto no encontrado")
    private String mensaje;

    @Schema(description = "Lista de detalles adicionales del error",
            example = "[\"El producto con id 5 no existe\"]")
    private List<String> detalles;
}
