package com.tienda.dtos;

import com.tienda.enums.MotivoMovimiento;
import com.tienda.enums.TipoCambio;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Historial genérico de cambios (stock o precio) sobre productos, marcas o proveedores")
public class MovimientoStockDTO {

    @Schema(description = "Identificador único del movimiento de stock. Es generado automáticamente por el sistema",
            example = "12")
    private Long id;

    @Schema(description = "Tipo de cambio: STOCK o PRECIO", example = "PRECIO")
    private TipoCambio tipoCambio;

    @Schema(description = "Valor anterior (stock o precio)", example = "100.00")
    private Double valorAnterior;

    @Schema(description = "Valor actual (stock o precio)", example = "120.00")
    private Double valorActual;

    @Schema(description = "Cantidad movida o Porcentaje de cambio. Positivo=aumento, Negativo=disminución", example = "20.0")
    private Integer valorNumerico;

    @Schema(description = "Fecha y hora exacta en la que se registró el movimiento de stock",
            example = "2024-01-24T10:30:00")
    private LocalDateTime fecha;

    @Schema(description = "Identificador del producto sobre el cual se realizó el movimiento de stock",
            example = "3")
    private Long productoId;

    @Schema(description = "Identificador del usuario que realizó el movimiento de stock. Puede ser nulo si el movimiento fue automático",
            example = "1",
            nullable = true)
    private Long userId;

    @Schema(description = "Nombre del producto afectado",
            example = "Base Líquida Infallible 24H")
    private String nombreProducto;

    @Schema(description = "Nombre del usuario que realizó la acción",
            example = "admin_ventas",
            nullable = true)
    private String nombreUsuario;

    @Schema(description = "Marca afectada (si es cambio masivo)", example = "1", nullable = true)
    private Long marcaId;

    @Schema(description = "Nombre marca", example = "L'Oréal", nullable = true)
    private String nombreMarca;

    @Schema(description = "ID del proveedor (si es cambio masivo por proveedor)", example = "5", nullable = true)
    private Long proveedorId;

    @Schema(description = "Nombre proveedor", example = "Distribuidora Sur", nullable = true)
    private String nombreProveedor;

    @Schema(description = "Motivo del cambio", example = "AJUSTE_PRECIO_MANUAL")
    private MotivoMovimiento motivo;
}
