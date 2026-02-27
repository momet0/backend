package com.tienda.dtos.promocion;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "Modelo para crear o actualizar una promoción")
public class PromocionRequest extends PromocionBaseDTO{

    @NotEmpty(message = "La promoción debe incluir al menos un producto")
    @Valid
    @Schema(description = "Lista de productos y cantidades que componen la promoción")
    private List<PromocionItemRequest> items;
}
