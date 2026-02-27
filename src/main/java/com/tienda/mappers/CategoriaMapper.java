package com.tienda.mappers;

import com.tienda.dtos.CategoriaDTO;
import com.tienda.models.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    CategoriaDTO toDTO(Categoria categoria);

    @Mapping(target = "productos", ignore = true)
    Categoria toEntity(CategoriaDTO dto);

    List<CategoriaDTO> toDTOList(List<Categoria> categorias);
}
