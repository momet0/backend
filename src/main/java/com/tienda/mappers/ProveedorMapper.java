package com.tienda.mappers;

import com.tienda.dtos.ProveedorDTO;
import com.tienda.models.Proveedor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProveedorMapper {

    ProveedorDTO toDTO(Proveedor proveedor);

    @Mapping(target = "listaProductos", ignore = true)
    Proveedor toEntity(ProveedorDTO proveedor);

    List<ProveedorDTO> toDTOList(List<Proveedor> listaProveedores);

}
