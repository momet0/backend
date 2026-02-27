package com.tienda.dtos.promocion;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Respuesta detallada con la información de una promoción")
public class PromocionResponse extends PromocionBaseDTO{

    @Schema(description = "Lista detallada de productos incluidos con su información completa")
    private List<PromocionItemResponse> items;
}