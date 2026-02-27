package com.tienda.services.impl;

import com.tienda.dtos.promocion.PromocionItemRequest;
import com.tienda.dtos.promocion.PromocionRequest;
import com.tienda.dtos.promocion.PromocionResponse;
import com.tienda.enums.TipoPromocion;
import com.tienda.mappers.PromocionMapper;
import com.tienda.models.Producto;
import com.tienda.models.Promocion;
import com.tienda.models.PromocionProducto;
import com.tienda.repositories.ProductoRepository;
import com.tienda.repositories.PromocionRepository;
import com.tienda.repositories.specs.PromocionSpecs;
import com.tienda.services.IPromocionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromocionService implements IPromocionService {

    private final PromocionRepository promocionRepository;
    private final ProductoRepository productoRepository;
    private final PromocionMapper promocionMapper;

    @Override
    @Transactional
    public PromocionResponse crearPromocion(PromocionRequest promocion) {
        validarItems(promocion.getItems());

        Promocion promo = promocionMapper.toEntity(promocion);
        vincularProductos(promo, promocion.getItems());
        configurarCantidadMinima(promo);

        return promocionMapper.toDTO(promocionRepository.save(promo));
    }

    public Page<PromocionResponse> listarPromociones(Boolean activo, TipoPromocion tipo,
                                                     int pagina, int tamaño, String ordenarPor,
                                                     String direccion, String nombreBusqueda) {
        Sort.Direction sortDirection = "DESC".equalsIgnoreCase(direccion)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(sortDirection,ordenarPor);
        Pageable pageable = PageRequest.of(pagina,tamaño,sort);
        Specification<Promocion> specs = nombreBusqueda != null ? PromocionSpecs.porFiltros(activo,tipo, sinAcentos(nombreBusqueda)):
                PromocionSpecs.porFiltros(activo,tipo,nombreBusqueda);
        Page<Promocion> promociones = promocionRepository.findAll(specs,pageable);
        return promociones.map(promocionMapper::toDTO);
    }

    @Override
    public PromocionResponse obtenerPromocionPorId(Long id) {

        return promocionMapper.toDTO(promocionRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Producto no encontrado")));
    }

    public List<PromocionResponse> obtenerPromocionProductoId(Long id){
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        return promocionMapper.toDTOList(promocionRepository.findByProductosDetalleProductoIdAndActivoTrue(id));
    }

    @Override
    @Transactional
    public PromocionResponse actualizarPromocion(Long id, PromocionRequest promo) {
        Promocion prom = promocionRepository.findById(id).orElseThrow( () -> new RuntimeException("Promocion no encontrada"));

        //Campos basicos
        prom.setNombre(promo.getNombre());
        prom.setPrecioFinal(promo.getPrecioFinal());
        prom.setTipoPromocion(promo.getTipoPromocion());
        prom.setActivo(promo.getActivo());

        if(promo.getItems() != null && !promo.getItems().isEmpty()){
            prom.getProductosDetalle().clear();
            vincularProductos(prom, promo.getItems());
        }

        configurarCantidadMinima(prom);
        return promocionMapper.toDTO(promocionRepository.save(prom));
    }

    @Override
    @Transactional
    public PromocionResponse cambiarEstadoActivo(Long id) {
        Promocion promo = promocionRepository.findById(id).orElseThrow(() -> new RuntimeException("Promocion no encontrada"));

        promo.setActivo(!promo.getActivo());

        return promocionMapper.toDTO(promocionRepository.save(promo));
    }

    private void validarItems(List<PromocionItemRequest> items){
        if(items == null || items.isEmpty()){
            throw new RuntimeException("Debe incluirse al menos un producto con su cantidad.");
        }
    }

    private void vincularProductos(Promocion promo, List<PromocionItemRequest> items){
        //Buscar todos los productos de una vez en lugar de 1 a 1
        List<Long> ids = items.stream().map(PromocionItemRequest::getProductoId).toList();
        List<Producto> productos = productoRepository.findAllById(ids);

        if(productos.size() != ids.size()){
            throw new RuntimeException("Uno o más productos no fueron encontrados");
        }

        //Mapeo
        List<PromocionProducto> detalles = items.stream().map(item -> {
            Producto producto = productos.stream()
                    .filter( p -> p.getId().equals(item.getProductoId()))
                    .findFirst()
                    .get();

            PromocionProducto pp = new PromocionProducto();
            pp.setProducto(producto);
            pp.setCantidad(item.getCantidad());
            pp.setPromocion(promo);
            return pp;
        }).collect(Collectors.toList());

        promo.setProductosDetalle(detalles);
    }

    private void configurarCantidadMinima(Promocion promo){
        if(TipoPromocion.COMBO.equals(promo.getTipoPromocion())){
            promo.setCantidadMinima(1);
        }
        // si no es combo, mantiene la cantidad minima que viene en el request
    }

    private String sinAcentos(String nombreBusqueda){
        return Normalizer.normalize(nombreBusqueda, Normalizer.Form.NFD)
                .replaceAll("\\p{InCOMBINING_DIACRITICAL_MARKS}+","");
    }
}
