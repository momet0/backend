package com.tienda.dtos.promocion;

import com.tienda.enums.TipoPromocion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PromocionBaseDTO {
    @Schema(description = "Identificador único de la promoción", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "El nombre de la promoción es obligatorio")
    @Schema(description = "Nombre comercial de la oferta", example = "Combo Tintura + Oxidante")
    private String nombre;

    @NotNull(message = "Debes especificar el tipo de promoción")
    @Schema(description = "Tipo de lógica aplicada (COMBO o DESCUENTO)", example = "COMBO")
    private TipoPromocion tipoPromocion;

    @PositiveOrZero(message = "La cantidad mínima no puede ser negativa")
    @Schema(description = "Cantidad mínima de productos para activar el beneficio", example = "2")
    private Integer cantidadMinima;

    @PositiveOrZero(message = "El precio final no puede ser negativo")
    @Schema(description = "Precio total final de la promoción", example = "5500.50")
    private Double precioFinal;

    @Schema(description = "Estado de disponibilidad de la promoción", example = "true")
    private Boolean activo;
}
