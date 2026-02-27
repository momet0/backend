package com.tienda.mappers;


import com.tienda.dtos.producto.ProductoDTO;
import com.tienda.models.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = ProductoMapperConfig.class)
public interface ProductoMapper {

    @Mapping(source = "marca.nombre", target = "marca")
    @Mapping(source = "categoria.nombre", target = "categoria")
    @Mapping(source = "proveedor.nombre", target = "proveedor")
    ProductoDTO toDTO(Producto producto);

    List<ProductoDTO> toDTOList(List<Producto> productos);
}
