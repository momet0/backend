package com.tienda.dtos.producto;

import com.tienda.enums.TipoOperacionPrecio;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "Solicitud para modificar precios masivamente por proveedor o marca",
        example = """
                {
                  "porcentaje": 15,
                  "tipoOperacion": "AUMENTO",
                  "marcaId": 1
                }
                """
)public class ModificarPrecioRequest {

    @NotNull(message = "El porcentaje es obligatorio")
    @Positive(message = "El porcentaje debe ser positivo")
    @Schema(description = "Porcentaje de modificación (siempre positivo, se trunca los decimales)", example = "10")
    private int porcentaje;

    @NotNull(message = "El tipo de operación es obligatorio")
    @Schema(description = "Tipo de operación: AUMENTO o DESCUENTO", example = "AUMENTO")
    private TipoOperacionPrecio tipoOperacion;

    @Schema(description = "ID del proveedor (si se filtra por proveedor)", example = "1")
    private Long proveedorId;

    @Schema(description = "ID de la marca (si se filtra por marca)", example = "2")
    private Long marcaId;
}