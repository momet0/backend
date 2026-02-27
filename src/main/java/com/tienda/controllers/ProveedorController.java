package com.tienda.controllers;

import com.tienda.dtos.ProveedorDTO;
import com.tienda.models.Proveedor;
import com.tienda.services.IProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/proveedores")
@RequiredArgsConstructor
@Tag(name = "Proveedores", description = "Endpoints para gestión de proveedores")
public class ProveedorController {

    private final IProveedorService proveedorService;

    @GetMapping("/")
    @Operation(
            summary = "Listar proveedores",
            description = "Devuelve la lista completa de proveedores registrados en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de proveedores obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    [
                      {
                        "id": 1,
                        "nombre": "L'Oréal Argentina SA"
                      },
                      {
                        "id": 2,
                        "nombre": "Procter & Gamble Argentina"
                      },
                      {
                        "id": 3,
                        "nombre": "Cosméticos Internacionales SRL"
                      },
                      {
                        "id": 4,
                        "nombre": "Laboratorios Beiersdorf"
                      }
                    ]
                    """
                            )
                    )
            )
    })
    public ResponseEntity<List<ProveedorDTO>> listarProveedores(){
        return ResponseEntity.ok(proveedorService.listarProveedores());
    }

    @GetMapping("/{nombre}")
    @Operation(
            summary = "Listar proveedores por nombre",
            description = "Busca proveedores que contengan el texto especificado en su nombre"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Proveedores encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    [
                      {
                        "id": 1,
                        "nombre": "L'Oréal Argentina SA"
                      }
                    ]
                    """
                            )
                    )
            )
    })
    public ResponseEntity<List<ProveedorDTO>> listarProveedoresPorNombre(
            @Parameter(description = "Texto para buscar en nombre del proveedor") @PathVariable String nombre){
        return ResponseEntity.ok(proveedorService.listarProveedorPorNombre(nombre));
    }

    @GetMapping("/productos")
    @Operation(
            summary = "Listar proveedores por productos",
            description = "Busca proveedores que suministren los productos especificados"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Proveedores encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    [
                      {
                        "id": 1,
                        "nombre": "L'Oréal Argentina SA"
                      }
                    ]
                    """
                            )
                    )
            )
    })
    public ResponseEntity<List<ProveedorDTO>> listarProveedoresPorProductos(
            @Parameter(description = "Lista de nombres de productos") @RequestParam Collection<String> listaProductos){
        return ResponseEntity.ok(proveedorService.listarProveedorPorListaDeProductos(listaProductos));
    }

    @PostMapping("/")
    @Operation(
            summary = "Crear proveedor",
            description = "Registra un nuevo proveedor en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Proveedor creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "id": 5,
                      "nombre": "Nuevo Proveedor SA"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de proveedor inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 400,
                      "error": "Bad Request",
                      "message": "El nombre del proveedor es requerido"
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<ProveedorDTO> crearProveedor(
            @Parameter(description = "Datos del nuevo proveedor") @RequestBody ProveedorDTO proveedor){
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorService.crearProveedor(proveedor));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar proveedor",
            description = "Actualiza los datos de un proveedor existente mediante su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Proveedor actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "id": 1,
                      "nombre": "L'Oréal Argentina Actualizada"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Proveedor no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 404,
                      "error": "Not Found",
                      "message": "Proveedor no encontrado"
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<ProveedorDTO> actualizarProveedor(
            @Parameter(description = "ID del proveedor") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del proveedor") @RequestBody ProveedorDTO proveedor){
        return ResponseEntity.ok(proveedorService.actualizarProveedor(id, proveedor));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar proveedor",
            description = "Elimina un proveedor del sistema si no tiene productos asociados"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Proveedor eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Proveedor no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 404,
                      "error": "Not Found",
                      "message": "Proveedor no encontrado"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Proveedor tiene productos asociados",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 409,
                      "error": "Conflict",
                      "message": "No se puede eliminar el proveedor porque tiene productos asociados"
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<Void> eliminarProveedor(@Parameter(description = "ID del proveedor a eliminar") @PathVariable Long id){
        proveedorService.borrarProveedor(id);
        return ResponseEntity.noContent().build();
    }
}
