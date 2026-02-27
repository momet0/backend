package com.tienda.controllers;

import com.tienda.dtos.CategoriaDTO;
import com.tienda.mappers.CategoriaMapper;
import com.tienda.models.Categoria;
import com.tienda.services.ICategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "Endpoints para gestión de categorías")
public class CategoriaController {

    private final ICategoriaService categoriaService;

    @PostMapping("/")
    @Operation(
            summary = "Crear categoría",
            description = "Registra una nueva categoría en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Categoría creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "id": 6,
                      "nombre": "Accesorios capilares"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de categoría inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 400,
                      "error": "Bad Request",
                      "message": "El nombre de la categoría es requerido"
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<CategoriaDTO> crearCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crearCategoria(categoriaDTO));
    }

    @GetMapping("/")
    @Operation(
            summary = "Listar categorías",
            description = "Devuelve la lista completa de categorías registradas en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de categorías obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    [
                      {
                        "id": 1,
                        "nombre": "Maquillaje"
                      },
                      {
                        "id": 2,
                        "nombre": "Skincare"
                      },
                      {
                        "id": 3,
                        "nombre": "Cuidado capilar"
                      },
                      {
                        "id": 4,
                        "nombre": "Fragancias"
                      },
                      {
                        "id": 5,
                        "nombre": "Accesorios de belleza"
                      }
                    ]
                    """
                            )
                    )
            )
    })
    public ResponseEntity<List<CategoriaDTO>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.obtenerCategorias());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener categoría por ID",
            description = "Recupera una categoría específica por su identificador único"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoría encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "id": 1,
                      "nombre": "Maquillaje"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoría no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 404,
                      "error": "Not Found",
                      "message": "Categoría no encontrada"
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<CategoriaDTO> encontrarCategoriaPorId(
            @Parameter(description = "ID de la categoría") @PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.obtenerCategoriaPorId(id));
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(
            summary = "Obtener categoría por nombre",
            description = "Recupera una categoría a partir de su nombre"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoría encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "id": 1,
                      "nombre": "Maquillaje"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoría no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 404,
                      "error": "Not Found",
                      "message": "Categoría no encontrada"
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<CategoriaDTO> encontrarCategoriaPorNombre(
            @Parameter(description = "Nombre de la categoría") @PathVariable String nombre){
        return ResponseEntity.ok(categoriaService.obtenerCategoriaPorNombre(nombre));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar categoría",
            description = "Actualiza los datos de una categoría existente mediante su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoría actualizada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "id": 1,
                      "nombre": "Maquillaje profesional"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoría no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 404,
                      "error": "Not Found",
                      "message": "Categoría no encontrada"
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<CategoriaDTO> actualizarCategoria(
            @Parameter(description = "ID de la categoría") @PathVariable Long id,
            @Valid @RequestBody CategoriaDTO categoriaDTO){
        return ResponseEntity.ok(categoriaService.actualizarCategoria(id, categoriaDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar categoría",
            description = "Elimina una categoría del sistema si no tiene productos asociados"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Categoría eliminada exitosamente"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Categoría tiene productos asociados",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 409,
                      "error": "Conflict",
                      "message": "No se puede eliminar la categoría porque tiene productos asociados"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoría no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 404,
                      "error": "Not Found",
                      "message": "Categoría no encontrada"
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<Void> borrarCategoria(@Parameter(description = "ID de la categoría a eliminar") @PathVariable Long id){
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}

