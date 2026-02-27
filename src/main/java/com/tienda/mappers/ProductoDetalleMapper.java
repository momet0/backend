package com.tienda.mappers;


import com.tienda.dtos.producto.ProductoDetalleDTO;
import com.tienda.models.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;


@Mapper(config = ProductoMapperConfig.class, uses = {CategoriaMapper.class, ProveedorMapper.class, PromocionMapper.class, MarcaMapper.class})
public interface ProductoDetalleMapper {

    ProductoDetalleDTO toDTO(Producto producto);

    @Mapping(target = "marca", ignore = true)
    @Mapping(target = "nombreBusqueda", ignore = true)
    @Mapping(target = "promociones", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "proveedor", ignore = true)
    Producto toEntity(ProductoDetalleDTO dto);

    List<ProductoDetalleDTO> toDTOList(List<Producto> productos);

}

