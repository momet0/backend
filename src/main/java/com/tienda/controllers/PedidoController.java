package com.tienda.controllers;

import com.tienda.dtos.pedido.PedidoRequest;
import com.tienda.dtos.pedido.PedidoResponse;
import com.tienda.dtos.pedido.PedidoResumenDTO;
import com.tienda.enums.EstadoPedido;
import com.tienda.enums.TipoVenta;
import com.tienda.services.IPedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/pedido")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Endpoints para gestión de pedidos")
public class PedidoController {

    private final IPedidoService pedidoServiceImpl;

    @GetMapping("/")
    @Operation(
            summary = "Obtener pedidos paginados",
            description = "Recupera lista de pedidos con filtros opcionales por cliente, tipo venta, estado y rango de fechas"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pedidos paginada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "content": [
                        {
                          "id": 1,
                          "fecha": "2024-01-21T10:30:00",
                          "total": 7300.00,
                          "nombreCliente": "María García",
                          "estado": "ENTREGADO",
                          "tipoVenta": "LOCAL",
                          "direccion": "Tienda central",
                          "codigoPostal": null,
                          "localidad": null,
                          "metodoPago": "EFECTIVO",
                          "productos": []
                        }
                      ],
                      "pageable": {
                        "pageNumber": 0,
                        "pageSize": 10
                      },
                      "totalElements": 25,
                      "totalPages": 3
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros inválidos",
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
    public ResponseEntity<Page<PedidoResponse>> obtenerPedidos(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int tamaño,
            @Parameter(description = "Campo de ordenamiento") @RequestParam(defaultValue = "id") String ordernarPor,
            @Parameter(description = "Dirección de ordenamiento") @RequestParam(defaultValue = "DESC") String sortDireccion,
            @Parameter(description = "Nombre del cliente para filtrar") @RequestParam(required = false) String nombreCliente,
            @Parameter(description = "Tipo de venta para filtrar") @RequestParam(required = false) TipoVenta tipoVenta,
            @Parameter(description = "Estado del pedido para filtrar") @RequestParam(required = false) EstadoPedido estadoPedido,
            @Parameter(description = "Fecha inicial del filtro (formato YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @Parameter(description = "Fecha final del filtro (formato YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta){
        return ResponseEntity.ok(pedidoServiceImpl.obtenerPedidos(pagina,tamaño,ordernarPor,sortDireccion,nombreCliente,tipoVenta,estadoPedido,desde,hasta));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener pedido por ID",
            description = "Recupera un pedido específico por su identificador único"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pedido encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "id": 1,
                      "fecha": "2024-01-21T10:30:00",
                      "total": 7300.00,
                      "nombreCliente": "María García",
                      "estado": "ENTREGADO",
                      "tipoVenta": "LOCAL",
                      "direccion": "Tienda central",
                      "codigoPostal": null,
                      "localidad": null,
                      "metodoPago": "EFECTIVO",
                      "productos": []
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 404,
                      "error": "Not Found",
                      "message": "Pedido no encontrado"
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<PedidoResponse> obtenerPedidoPorId(@Parameter(description = "ID del pedido") @PathVariable Long id){
        return ResponseEntity.ok(pedidoServiceImpl.obtenerPedidoPorId(id));
    }

    @PostMapping("/")
    @Operation(
            summary = "Crear pedido",
            description = "Crea un nuevo pedido en el sistema con los productos especificados"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Pedido creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "id": 25,
                      "fecha": "2024-01-24T12:30:00",
                      "total": 13500.00,
                      "nombreCliente": "Juan Pérez",
                      "estado": "PENDIENTE",
                      "tipoVenta": "ENVIO",
                      "direccion": "Av. Siempre Viva 742",
                      "codigoPostal": "3200",
                      "localidad": "Concordia",
                      "metodoPago": "EFECTIVO",
                      "productos": []
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de pedido inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 400,
                      "error": "Bad Request",
                      "message": "Tipo de venta obligatorio"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 404,
                      "error": "Not Found",
                      "message": "Producto no encontrado"
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<PedidoResponse> crearPedido(@Valid @RequestBody PedidoRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoServiceImpl.crearPedido(request));
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Cambiar estado del pedido",
            description = "Actualiza el estado de un pedido existente según las reglas de transición definidas"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "id": 1,
                      "fecha": "2024-01-21T10:30:00",
                      "total": 7300.00,
                      "nombreCliente": "María García",
                      "estado": "CANCELADO",
                      "tipoVenta": "LOCAL",
                      "direccion": "Tienda central",
                      "codigoPostal": null,
                      "localidad": null,
                      "metodoPago": "EFECTIVO",
                      "productos": []
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 404,
                      "error": "Not Found",
                      "message": "Pedido no encontrado"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Transición de estado inválida",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 400,
                      "error": "Bad Request",
                      "message": "No se puede cambiar el estado de PENDIENTE a CANCELADO para una venta local"
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<PedidoResponse> cambiarEstado(
            @Parameter(description = "ID del pedido") @PathVariable Long id,
            @Parameter(description = "Nuevo estado del pedido") @RequestParam EstadoPedido estadoPedido){
        return ResponseEntity.ok(pedidoServiceImpl.cambiarEstado(id,estadoPedido));
    }

    @GetMapping("/resumen")
    @Operation(
            summary = "Obtener resumen de pedidos",
            description = "Retorna un resumen con la cantidad de pedidos por estado y el total general"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Resumen obtenido exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "total": 150,
                      "en_preparacion": 12,
                      "pendientes": 8,
                      "entregados": 110,
                      "cancelados": 20
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<PedidoResumenDTO> resumenPedidos() {
        return ResponseEntity.ok(pedidoServiceImpl.obtenerResumen());
    }

    @GetMapping("/{id}/ticket")
    @Operation(
            summary = "Generar ticket PDF",
            description = "Genera y descarga un archivo PDF con el ticket de venta del pedido especificado"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ticket PDF generado exitosamente",
                    content = @Content(mediaType = "application/pdf")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pedido no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 404,
                      "error": "Not Found",
                      "message": "Pedido no encontrado"
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<byte[]> descargarTicket(@Parameter(description = "ID del pedido para el ticket") @PathVariable Long id) {
        byte[] pdf = pedidoServiceImpl.generarTicketPDF(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=ticket_pedido.pdf")
                .body(pdf);
    }
}