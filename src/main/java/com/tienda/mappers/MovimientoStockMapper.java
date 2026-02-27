package com.tienda.mappers;

import com.tienda.dtos.MovimientoStockDTO;
import com.tienda.models.MovimientoStock;
import com.tienda.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovimientoStockMapper {

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "producto.nombre", target = "nombreProducto")
    @Mapping(source = "marca.id", target = "marcaId")
    @Mapping(source = "marca.nombre", target ="nombreMarca")
    @Mapping(source = "proveedor.id", target = "proveedorId")
    @Mapping(source = "proveedor.nombre", target = "nombreProveedor")
    @Mapping(target = "userId", expression = "java(mov.getUser() != null ? mov.getUser().getId() : null)")
    @Mapping(target = "nombreUsuario", expression = "java(mov.getUser() != null ? mov.getUser().getUsername() : \"SISTEMA\")")
    MovimientoStockDTO toDTO(MovimientoStock mov);

    List<MovimientoStockDTO> toDTOList(List<MovimientoStock> movs);

    // Convierte DTO → entidad (para guardar en DB)
    @Mapping(source = "productoId", target = "producto.id")
    @Mapping(source = "marcaId", target = "marca.id")
    @Mapping(source = "proveedorId", target = "proveedor.id")
    @Mapping(target = "user", expression = "java(dto.getUserId() != null ? toUser(dto.getUserId()) : null)")
    MovimientoStock toEntity(MovimientoStockDTO dto);

    // Método auxiliar para construir User solo con id
    default User toUser(Long id) {
        if(id == null) return null;
        User u = new User();
        u.setId(id);
        return u;
    }
}


