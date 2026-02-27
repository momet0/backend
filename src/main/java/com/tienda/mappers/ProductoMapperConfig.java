package com.tienda.mappers;

import com.tienda.dtos.producto.ProductoBaseDTO;
import com.tienda.models.Marca;
import com.tienda.models.Producto;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@MapperConfig(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductoMapperConfig {
    //Regla global: Siempre que el DTO tenga un string 'marca', MapStruct va a buscar el 'nombre' de la entidad 'marca'
    @Mapping(source = "marca.nombre", target = "marca")
    void toBaseDTO(Producto entity, @org.mapstruct.MappingTarget ProductoBaseDTO dto);

    default String map(Marca marca) {
        return (marca != null) ? marca.getNombre() : null;
    }
}
