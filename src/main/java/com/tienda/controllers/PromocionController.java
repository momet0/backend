package com.tienda.controllers;

import com.tienda.dtos.promocion.PromocionRequest;
import com.tienda.dtos.promocion.PromocionResponse;
import com.tienda.enums.TipoPromocion;
import com.tienda.services.IPromocionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promociones")
@RequiredArgsConstructor
@Tag(name = "Promociones", description = "Gestión de ofertas y promociones para los productos de la tienda")
public class PromocionController {

    private final IPromocionService promocionService;

    @Operation(
            summary = "Listar todas las promociones",
            description = "Permite filtrar promociones por estado activo/inactivo, por tipo de promoción y por su nombre"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de promociones obtenida exitosamente",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = """
                {
                  "content": [
                    {
                      "id": 1,
                      "nombre": "Descuento de Verano",
                      "descripcion": "20% de descuento en productos seleccionados",
                      "activo": true,
                      "tipo": "DESCUENTO",
                      "porcentajeDescuento": 20.0,
                      "fechaInicio": "2024-01-01",
                      "fechaFin": "2024-02-01"
                    }
                  ],
                  "pageable": {
                    "pageNumber": 0,
                    "pageSize": 10
                  },
                  "totalElements": 5,
                  "totalPages": 1
                }
                """
                    )
            )
    )
    @GetMapping("/")
    public ResponseEntity<Page<PromocionResponse>> listarPromociones(
            @Parameter(description = "Filtrar por promociones activas") @RequestParam(required = false) Boolean activo,
            @Parameter(description = "Filtrar por tipo (ej. COMBO, DESCUENTO)") @RequestParam(required = false) TipoPromocion tipo,
            @Parameter(description = "Filtrar por nombre de promoción") @RequestParam(required = false) String nombreBusqueda,
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int tamaño,
            @Parameter(description = "Campo de ordenamiento") @RequestParam(defaultValue = "id") String ordenarPor,
            @Parameter(description = "Dirección de ordenamiento") @RequestParam(defaultValue = "ASC") String direccion
    ){
        return ResponseEntity.ok(promocionService.listarPromociones(activo, tipo, pagina, tamaño, ordenarPor, direccion,nombreBusqueda));
    }

    @Operation(summary = "Obtener promoción por ID", description = "Busca una promoción específica")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Promoción encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                        {
                          "id": 1,
                          "nombre": "Descuento de Verano",
                          "descripcion": "20% de descuento en productos seleccionados",
                          "activo": true,
                          "tipo": "DESCUENTO",
                          "porcentajeDescuento": 20.0,
                          "fechaInicio": "2024-01-01",
                          "fechaFin": "2024-02-01",
                          "productos": []
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Promoción no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                        {
                          "timestamp": "2024-01-24T10:30:00",
                          "status": 404,
                          "error": "Not Found",
                          "message": "Promoción no encontrada"
                        }
                        """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<PromocionResponse> obtenerPromocionPorId(@PathVariable Long id){
        return ResponseEntity.ok(promocionService.obtenerPromocionPorId(id));
    }

    @Operation(
            summary = "Crear nueva promoción",
            description = "Registra una nueva oferta con sus respectivos items en el sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Promoción creada exitosamente",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = """
                {
                  "id": 6,
                  "nombre": "Nueva Promoción",
                  "descripcion": "Promoción especial de lanzamiento",
                  "activo": true,
                  "tipo": "COMBO",
                  "porcentajeDescuento": null,
                  "fechaInicio": "2024-02-01",
                  "fechaFin": "2024-03-01",
                  "productos": []
                }
                """
                    )
            )
    )
    @PostMapping("/")
    public ResponseEntity<PromocionResponse> crearPromocion(@RequestBody PromocionRequest promocion){
        return ResponseEntity.ok(promocionService.crearPromocion(promocion));
    }

    @Operation(summary = "Alternar estado activo", description = "Cambia el estado de la promoción (de activo a inactivo y viceversa)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado cambiado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Promoción no encontrada"
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<PromocionResponse> cambiarEstadoActivo(@PathVariable Long id){
        return ResponseEntity.ok(promocionService.cambiarEstadoActivo(id));
    }

    @Operation(summary = "Actualizar promoción", description = "Modifica los datos de una promoción existente mediante su ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Promoción actualizada exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Promoción no encontrada"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<PromocionResponse> actualizarPromocion(@PathVariable Long id, @RequestBody PromocionRequest promo){
        return ResponseEntity.ok(promocionService.actualizarPromocion(id, promo));
    }

    @GetMapping("/producto/{id}")
    @Operation(summary = "Obtener promociones activas por producto ID")
    public ResponseEntity<List<PromocionResponse>> obtenerPromocionesPorProductoId(
            @Parameter(description = "ID del producto") @PathVariable Long id){
        return ResponseEntity.ok(promocionService.obtenerPromocionProductoId(id));
    }
}