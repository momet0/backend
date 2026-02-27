package com.tienda.controllers;

import com.tienda.dtos.MovimientoStockDTO;
import com.tienda.enums.MotivoMovimiento;
import com.tienda.enums.TipoCambio;
import com.tienda.services.IMovimientoStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
@Tag(name = "Movimientos de Stock", description = "Endpoints para registrar y consultar movimientos de stock")
public class MovimientoStockController {

    private final IMovimientoStockService movimientoService;

    @GetMapping("/")
    @Operation(
            summary = "Listar movimientos de stock con filtros avanzados",
            description = "Obtiene el historial de movimientos de stock permitiendo filtrar por múltiples criterios. " +
                    "Soporta búsqueda textual por producto/usuario y rangos de fechas"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de movimientos obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                        {
                          "content": [
                            {
                              "id": 1,
                              "cantidad": 20,
                              "fecha": "2024-01-20T09:00:00",
                              "motivo": "REABASTECIMIENTO",
                              "productoId": 6,
                              "nombreProducto": "Base Líquida Infallible 24H",
                              "userId": 2,
                              "nombreUsuario": "admin_ventas"
                            }
                          ],
                          "pageable": {
                            "pageNumber": 0,
                            "pageSize": 10
                          },
                          "totalElements": 1,
                          "totalPages": 1
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros de filtro inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                        {
                          "timestamp": "2024-01-24T10:30:00",
                          "status": 400,
                          "error": "Bad Request",
                          "message": "Formato de fecha inválido"
                        }
                        """
                            )
                    )
            )
    })
    public ResponseEntity<Page<MovimientoStockDTO>> listarMovimientos(
            @Parameter(description = "Número de página (0..N)")
            @RequestParam(defaultValue = "0") int pagina,

            @Parameter(description = "Cantidad de registros por página")
            @RequestParam(defaultValue = "10") int tamaño,

            @Parameter(description = "Campo por el cual ordenar (id, fecha, cantidad)")
            @RequestParam(defaultValue = "id") String ordenarPor,

            @Parameter(description = "Dirección del orden (ASC o DESC)")
            @RequestParam(defaultValue = "DESC") String direccion,

            @Parameter(description = "Filtrar por tipo: STOCK o PRECIO")
            @RequestParam(required = false) TipoCambio tipoCambio,

            @Parameter(description = "Filtrar por motivo específico")
            @RequestParam(required = false) MotivoMovimiento motivo,

            @Parameter(description = "Filtrar por ID exacto de producto")
            @RequestParam(required = false) Long productoId,

            @Parameter(description = "Filtrar por marca")
            @RequestParam(required = false) Long marcaId,

            @Parameter(description = "Filtrar por proveedor")
            @RequestParam(required = false) Long proveedorId,

            @Parameter(description = "Buscar por nombre de producto (parcial)")
            @RequestParam(required = false) String nombreProducto,

            @Parameter(description = "Buscar por nombre de usuario (parcial)")
            @RequestParam(required = false) String nombreUsuario,

            @Parameter(description = "Fecha inicial del rango (YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,

            @Parameter(description = "Fecha final del rango (YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        return ResponseEntity.ok(movimientoService.obtenerTodos(
                pagina, tamaño, ordenarPor, direccion, tipoCambio,
                motivo, productoId, marcaId, proveedorId, inicio, fin, nombreProducto, nombreUsuario));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener movimiento por ID",
            description = "Recupera un movimiento de stock específico por su identificador único"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Movimiento encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                            {
                              "id": 1,
                              "cantidad": 20,
                              "fecha": "2024-01-20T09:00:00",
                              "motivo": "REABASTECIMIENTO",
                              "productoId": 6,
                              "nombreProducto": "Máscara de Pestañas",
                              "userId": null,
                              "nombreUsuario": "SISTEMA"
                            }
                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Movimiento no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                            {
                              "timestamp": "2024-01-24T10:30:00",
                              "status": 404,
                              "error": "Not Found",
                              "message": "Movimiento no encontrado"
                            }
                            """
                            )
                    )
            )
    })
    public ResponseEntity<MovimientoStockDTO> obtenerMovimiento(
            @Parameter(description = "ID del movimiento") @PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.obtenerPorId(id));
    }
}