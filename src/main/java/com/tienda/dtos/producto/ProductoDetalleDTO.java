package com.tienda.dtos.producto;

import com.tienda.dtos.CategoriaDTO;
import com.tienda.dtos.marca.MarcaResponse;
import com.tienda.dtos.promocion.PromocionResponse;
import com.tienda.dtos.ProveedorDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo que representa la información completa de un producto")
public class ProductoDetalleDTO extends ProductoBaseDTO {
    @NotBlank(message = "El codigo de barras del producto no puede estar vacio")
    @Schema(description = "Código de barras único", example = "1234567890123")
    private String codigoBarra;

    @NotNull(message = "La categoria del producto no puede estar vacia")
    @Schema(description = "Categoría detallada")
    @Valid
    private CategoriaDTO categoria;

    @NotNull(message = "La marca del producto no puede estar vacia")
    @Schema(description = "Marca detallada")
    @Valid
    private MarcaResponse marca;

    @NotNull(message = "El proveedor del producto no puede estar vacio")
    @Schema(description = "Proveedor detallado")
    @Valid
    private ProveedorDTO proveedor;

}
