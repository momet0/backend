package com.tienda.mappers;

import com.tienda.dtos.marca.MarcaRequest;
import com.tienda.dtos.marca.MarcaResponse;
import com.tienda.models.Marca;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MarcaMapper {
    MarcaResponse toResponse(Marca marca);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productos", ignore = true)
    Marca toEntity(MarcaRequest request);

    List<MarcaResponse> toResponseList(List<Marca> marcas);
}
