package com.tienda.mappers;


import com.tienda.dtos.promocion.PromocionProductoDTO;
import com.tienda.models.Promocion;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PromocionProductoMapper {

    PromocionProductoDTO toDTO(Promocion promocion);

    List<PromocionProductoDTO> toDTOList (List<Promocion> promociones);
}
