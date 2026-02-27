package com.tienda.controllers;

import com.tienda.dtos.producto.*;
import com.tienda.enums.ActividadProducto;
import com.tienda.enums.EstadoStock;
import com.tienda.services.IProductoService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/producto")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Endpoints para gestión de productos")
public class ProductoController {

    private final IProductoService productoService;

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Crear producto",
            description = "Registra un nuevo producto en el sistema con imagen opcional que se sube a Cloudinary"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "id": 15,
                      "nombre": "Base líquida nueva",
                      "precio": 5500.00,
                      "stock": 30,
                      "minStock": 5,
                      "descripcion": "Base de alta cobertura efecto matte",
                      "activo": true,
                      "nombreBusqueda": "base liquida nueva marca 30ml",
                      "linea": "base",
                      "marca": "Nueva Marca",
                      "tono": "Natural beige",
                      "tamano": "30ml",
                      "codigoBarra": "1234567890123",
                      "categoria": "maquillaje",
                      "proveedor": "Proveedor Nuevo",
                      "imagenUrl": "https://res.cloudinary.com/demo/image/upload/tienda_productos/ejemplo.jpg"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de producto inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "timestamp": "2024-01-24T10:30:00",
                      "status": 400,
                      "error": "Bad Request",
                      "message": "El nombre del producto es requerido"
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<ProductoDetalleDTO> crearProducto(
            @Parameter(description = "Datos del producto en formato JSON") @RequestPart("producto") @Valid ProductoDetalleDTO producto,
            @Parameter(description = "Archivo de imagen del producto") @RequestPart(value = "img", required = false) MultipartFile img){
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.guardarProducto(producto,img));
    }

    @GetMapping("/")
    @Operation(
            summary = "Listar productos con filtros",
            description = "Obtiene lista paginada de productos con opciones de filtrado por estado, nombre, categoría, proveedor, stock bajo"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de productos activos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "content": [
                        {
                          "id": 1,
                          "nombre": "Base líquida infallible 24h",
                          "precio": 4500.00,
                          "stock": 25,
                          "minStock": 5,
                          "activo": true,
                          "linea": "infallible",
                          "marca": "L'Oréal",
                          "tono": "Natural beige",
                          "tamano": "30ml",
                          "categoria": "maquillaje",
                          "proveedor": "L'Oréal Argentina SA",
                          "imagenUrl": "https://res.cloudinary.com/demo/image/upload/tienda_productos/base_liquida.jpg"
                        }
                      ],
                      "pageable": {
                        "pageNumber": 0,
                        "pageSize": 10
                      },
                      "totalElements": 15,
                      "totalPages": 2
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<Page<ProductoDTO>> listarProductos(
            @Parameter(description = "Estado de actividad") @RequestParam(required = false) ActividadProducto actividadProducto,
            @Parameter(description = "ALTO para listar productos con stock alto y BAJO para listar productos con stock bajo, no mandar nada para listar todo") @RequestParam(required = false)EstadoStock estadoStock,
            @Parameter(description = "Nombre del producto") @RequestParam(required = false) String nombreBusqueda,
            @Parameter(description = "Categoría a la que pertenece") @RequestParam(required = false) Long categoria,
            @Parameter(description = "Proveedor que vende el producto") @RequestParam(required = false) Long proveedor,
            @Parameter(description = "Marca del producto") @RequestParam(required = false) String marca,
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int tamaño,
            @Parameter(description = "Campo de ordenamiento") @RequestParam(defaultValue = "id") String ordenarPor,
            @Parameter(description = "Dirección de ordenamiento") @RequestParam(defaultValue = "ASC") String direccion){

        return ResponseEntity.ok(productoService.obtenerProductos(pagina, tamaño,ordenarPor,direccion,actividadProducto,nombreBusqueda,categoria,proveedor,estadoStock,marca));
    }


    @PatchMapping("/{id}")
    @Operation(
            summary = "Actualizar producto",
            description = "Actualiza los datos de un producto existente mediante su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "id": 1,
                      "nombre": "Base líquida actualizada",
                      "precio": 4800.00,
                      "stock": 30,
                      "minStock": 8,
                      "descripcion": "Base mejorada con nueva fórmula",
                      "activo": true,
                      "nombreBusqueda": "base liquida actualizada marca 30ml",
                      "linea": "infallible",
                      "marca": "L'Oréal",
                      "tono": "Natural beige actualizado",
                      "tamano": "30ml",
                      "codigoBarra": "3600523374581",
                      "categoria": "maquillaje",
                      "proveedor": "L'Oréal Argentina SA",
                      "imagenUrl": "https://res.cloudinary.com/demo/image/upload/tienda_productos/base_actualizada.jpg"
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
    public ResponseEntity<ProductoDetalleDTO> actualizarProducto (
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del producto") @RequestPart @Valid ProductoUpdateDTO producto,
            @Parameter(description = "Archivo de imagen del producto") @RequestPart(value = "img", required = false) MultipartFile img){
        return ResponseEntity.ok(productoService.actualizarProducto(id, producto, img));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar producto",
            description = "Cambia el estado de un producto a inactivo"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto eliminado exitosamente"
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void borrarProducto(@Parameter(description = "ID del producto a eliminar") @PathVariable Long id){
        productoService.suspenderProducto(id);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener producto por ID",
            description = "Recupera un producto específico por su identificador único"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "id": 1,
                      "nombre": "Base líquida infallible 24h",
                      "precio": 4500.00,
                      "stock": 25,
                      "minStock": 5,
                      "descripcion": "Base de alta cobertura con efecto matte de larga duración",
                      "activo": true,
                      "nombreBusqueda": "infallible l oreal base 30ml",
                      "linea": "infallible",
                      "marca": "L'Oréal",
                      "tono": "Natural beige",
                      "tamano": "30ml",
                      "codigoBarra": "3600523374581",
                      "categoria": {
                        "id": 1,
                        "nombre": "Maquillaje"
                      },
                      "proveedor": {
                        "id": 1,
                        "nombre": "L'Oréal Argentina SA"
                      },
                      "imagenUrl": "https://res.cloudinary.com/demo/image/upload/tienda_productos/base_infallible.jpg"
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
    public ResponseEntity<ProductoDetalleDTO> encontrarProductoPorId(@Parameter(description = "ID del producto") @PathVariable Long id){
        return ResponseEntity.ok(productoService.obtenerProductoPorId(id));
    }


    @GetMapping("/resumen")
    @Operation(
            summary = "Obtener resumen de productos",
            description = "Devuelve un resumen con estadísticas de productos totales, sin stock y con stock bajo"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Resumen de productos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                    {
                      "total": 50,
                      "conStock": 45,
                      "sinStock": 5,
                      "stockBajo": 8,
                      "activos": 48,
                      "inactivos": 2
                    }
                    """
                            )
                    )
            )
    })
    public ResponseEntity<ProductoResumenDTO> resumenProductos() {
        return ResponseEntity.ok(productoService.obtenerResumen());
    }

    @GetMapping("codigoBarras/{codigoBarra}")
    public ResponseEntity<ProductoDetalleDTO> obtenerPorCodigodeBarras(@PathVariable String codigoBarra){
        return ResponseEntity.ok(productoService.obtenerPorCodigodeBarras(codigoBarra));
    }

    @PatchMapping("/precios/modificar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Modificar precios por proveedor o marca",
            description = """
                Aplica aumento o descuento porcentual a productos. 
                • Porcentaje: entero positivo (decimales truncados)
                • Precio final: redondeado sin decimales
                • Filtro: solo marca o proveedor, no ambos
                """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Precios modificados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o ambos filtros")
    })
    public void modificarPrecios(@RequestBody @Valid ModificarPrecioRequest request) {
        productoService.modificarPrecios(request);
    }
}
