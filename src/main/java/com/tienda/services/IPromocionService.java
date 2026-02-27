package com.tienda.services;

import com.tienda.dtos.promocion.PromocionRequest;
import com.tienda.dtos.promocion.PromocionResponse;
import com.tienda.enums.TipoPromocion;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPromocionService {
    PromocionResponse crearPromocion(PromocionRequest promo);
    Page<PromocionResponse> listarPromociones(Boolean activo, TipoPromocion tipo,
                                              int pagina, int tamaño, String ordenarPor,
                                              String direccion, String nombreBusqueda);
    PromocionResponse obtenerPromocionPorId(Long id);
    PromocionResponse actualizarPromocion(Long id, PromocionRequest promo);
    PromocionResponse cambiarEstadoActivo(Long id);
    List<PromocionResponse> obtenerPromocionProductoId(Long id);
}
