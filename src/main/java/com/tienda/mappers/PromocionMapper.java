package com.tienda.mappers;

import com.tienda.dtos.promocion.PromocionItemResponse;
import com.tienda.dtos.promocion.PromocionRequest;
import com.tienda.dtos.promocion.PromocionResponse;
import com.tienda.models.Promocion;
import com.tienda.models.PromocionProducto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = ProductoMapper.class)
public interface PromocionMapper {

    @Mapping(source = "productosDetalle", target = "items")
    PromocionResponse toDTO(Promocion promo);

    @Mapping(target = "detalles", ignore = true)
    @Mapping(target = "productosDetalle", ignore = true)
    Promocion toEntity(PromocionRequest promo);

    List<PromocionResponse> toDTOList(List<Promocion> promociones);

    @Mapping(source = "producto", target = "producto")
    PromocionItemResponse toItemDTO(PromocionProducto pp);
}
