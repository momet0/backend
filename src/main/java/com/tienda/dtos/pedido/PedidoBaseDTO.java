package com.tienda.dtos.pedido;

import com.tienda.enums.MetodoPago;
import com.tienda.enums.TipoVenta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PedidoBaseDTO {
    @Schema(description = "Nombre del cliente", example = "Juan")
    private String nombreCliente;

    @Schema(description = "Teléfono de contacto", example = "3456123456")
    private String telefono;

    @NotNull(message = "Debes especificar el método de pago")
    @Schema(description = "Método de pago utilizado", example = "EFECTIVO")
    private MetodoPago metodoPago;

    @NotNull(message = "El tipo de venta (LOCAL/ENVIO) es obligatorio")
    @Schema(description = "Tipo de venta", example = "ENVIO")
    private TipoVenta tipoVenta;

    @Schema(description = "Dirección de entrega (obligatoria para ENVÍO)", example = "Av. San Martín 1234")
    private String direccion;

    @Schema(description = "Código postal", example = "3200")
    private String codigoPostal;

    @Schema(description = "Localidad de entrega", example = "Concordia")
    private String localidad;
}
