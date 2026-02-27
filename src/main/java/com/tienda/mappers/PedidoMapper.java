package com.tienda.mappers;


import com.tienda.dtos.pedido.PedidoResponse;
import com.tienda.models.DetallePedido;
import com.tienda.models.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface PedidoMapper {
    //Mapeo principal de Pedido
    @Mapping(source = "cliente", target = "nombreCliente") // 'cliente' en entidad -> 'nombreCliente' en DTO
    @Mapping(source = "estadoPedido", target = "estado") // Mapeo de enums
    @Mapping(source = "items", target = "items") // 'items' en entidad -> 'productos' en DTO
    PedidoResponse toResponse(Pedido pedido);

    //Mapeo de Detalle
    @Mapping(target = "descripcion", expression = "java(obtenerDescripcion(detalle))")
    @Mapping(target = "precioUnitario", expression = "java(obtenerPrecio(detalle))")
    @Mapping(target = "imagenUrl", expression = "java(obtenerImgUrl(detalle))")
    @Mapping(source = "subTotal", target = "precioFinal")
    @Mapping(target = "tipo", expression = "java(obtenerTipo(detalle))")
    @Mapping(target = "productos", expression = "java(mapearProductos(detalle))")
    PedidoResponse.DetalleResponse toDetalleResponse(DetallePedido detalle);

    //Logica auxiliar para descripcion
    default String obtenerDescripcion(DetallePedido detalle){
        if (detalle.getProducto() != null) return detalle.getProducto().getNombre();
        if (detalle.getPromocion() != null) return detalle.getPromocion().getNombre();
        return "Producto no identificado.";
    }

    //Logica auxiliar para precio
    default Double obtenerPrecio(DetallePedido detalle){
        if (detalle.getPromocion() != null) return detalle.getPromocion().getPrecioFinal();
        return detalle.getPrecioUnitario();
    }

    default String obtenerImgUrl(DetallePedido detalle){
        if (detalle.getProducto() != null) return detalle.getProducto().getImagenUrl();
        return null;
    }

    default String obtenerTipo(DetallePedido detalle){
        if (detalle.getPromocion() != null) return "PROMOCION";
        return "PRODUCTO";
    }

    default List<PedidoResponse.DetalleResponse.ProductosIncluidos> mapearProductos(DetallePedido detalle){

        // Si NO es promoción → devolver lista vacía (NO null)
        if (detalle.getPromocion() == null) {
            return java.util.Collections.emptyList();
        }

        return detalle.getPromocion()
                .getProductosDetalle()
                .stream()
                .map(pp -> {

                    PedidoResponse.DetalleResponse.ProductosIncluidos p =
                            new PedidoResponse.DetalleResponse.ProductosIncluidos();

                    p.setId(pp.getProducto().getId());
                    p.setNombre(pp.getProducto().getNombre());

                    int cantidadTotal =
                            pp.getCantidad() * detalle.getCantidad();

                    p.setCantidad(cantidadTotal);

                    return p;
                })
                .toList();
    }

}
