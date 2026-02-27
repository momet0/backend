package com.tienda.controllers;

import com.tienda.dtos.marca.MarcaRequest;
import com.tienda.dtos.marca.MarcaResponse;
import com.tienda.services.IMarcaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marcas")
@RequiredArgsConstructor
@Tag(name = "Marcas", description = "Catálogo de marcas para los productos de la tienda")
public class MarcaController {
    private final IMarcaService marcaService;

    @Operation(
            summary = "Listar todas las marcas",
            description = "Obtiene el listado completo de marcas disponibles para filtrar productos"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Listado recuperado exitosamente",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MarcaResponse.class),
                    examples = @ExampleObject(
                            value = """
                [
                  {
                    "id": 1,
                    "nombre": "L'Oréal"
                  },
                  {
                    "id": 2,
                    "nombre": "Maybelline"
                  }
                ]
                """
                    )
            )
    )
    @GetMapping("/")
    public ResponseEntity<List<MarcaResponse>> listar() {
        return ResponseEntity.ok(marcaService.listarTodas());
    }

    @Operation(
            summary = "Crear nueva marca",
            description = "Registra una marca en la base de datos para que luego pueda ser asignada a los productos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Marca creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MarcaResponse.class),
                            examples = @ExampleObject(
                                    value = """
                        {
                          "id": 3,
                          "nombre": "Nueva Marca"
                        }
                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos o nombre de marca duplicado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                        {
                          "timestamp": "2024-01-24T10:30:00",
                          "status": 400,
                          "error": "Bad Request",
                          "message": "El nombre de la marca ya existe"
                        }
                        """
                            )
                    )
            )
    })
    @PostMapping("/")
    public ResponseEntity<MarcaResponse> crear(@RequestBody MarcaRequest request) {
        return ResponseEntity.ok(marcaService.crear(request));
    }
}