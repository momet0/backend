package com.tienda.services;

import com.tienda.dtos.producto.*;
import com.tienda.enums.ActividadProducto;
import com.tienda.enums.EstadoStock;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductoService {
    Page<ProductoDTO> obtenerProductos(int pagina, int tamaño, String ordenarPor, String direccion,
                                       ActividadProducto actividadProducto, String nombreBusqueda, Long categoria,
                                       Long proveedor, EstadoStock estadoStock, String marca);
    ProductoDetalleDTO guardarProducto(ProductoDetalleDTO productoDetalleDto, MultipartFile img);
    ProductoDetalleDTO actualizarProducto(Long id, ProductoUpdateDTO productoUpdateDto, MultipartFile img);
    void suspenderProducto(Long id);
    ProductoDetalleDTO obtenerProductoPorId(Long id);

    List<ProductoDetalleDTO> findByCategoria(Long categoriaId);

    ProductoResumenDTO obtenerResumen();

    ProductoDetalleDTO obtenerPorCodigodeBarras(String codigoBarra);

    void modificarPrecios(ModificarPrecioRequest request);
}
