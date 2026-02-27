package com.tienda.dtos.producto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProductoBaseDTO {
    @Schema(description = "Identificador único del producto", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    @Schema(description = "Nombre comercial del producto", example = "Base Líquida Infallible 24H", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotBlank(message = "La linea del producto no puede estar vacia")
    @Schema(description = "Línea de producto", example = "Infallible")
    private String linea;

    @Schema(description = "Tono del producto (si aplica)", example = "Natural Beige")
    private String tono;

    @NotBlank(message = "El tamaño del producto no puede estar vacio")
    @Schema(description = "Tamaño del producto", example = "30ml")
    private String tamaño;

    @Schema(description = "URL de la imagen del producto almacenada en Cloudinary", example = "https://res.cloudinary.com/demo/image/upload/tienda_productos/base_liquida.jpg")
    private String imagenUrl;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Schema(description = "Precio unitario en moneda local", example = "4500.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double precio;

    @Schema(description = "Indica si el producto está activo o no", example = "true")
    private Boolean activo;

    @NotNull(message = "El stock inicial es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Schema(description = "Cantidad actual disponible en bodega", example = "25")
    private Integer stock;

    @NotNull(message = "El stock mínimo es obligatorio")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    @Schema(description = "Nivel mínimo de inventario", example = "5")
    private Integer minStock;
}
