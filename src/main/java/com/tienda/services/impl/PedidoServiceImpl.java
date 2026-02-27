package com.tienda.services.impl;



import com.tienda.dtos.pedido.PedidoRequest;
import com.tienda.dtos.pedido.PedidoResponse;
import com.tienda.enums.EstadoPedido;
import com.tienda.enums.MotivoMovimiento;
import com.tienda.enums.TipoVenta;
import com.tienda.mappers.PedidoMapper;
import com.tienda.models.*;
import com.tienda.repositories.PedidoRepository;
import com.tienda.repositories.ProductoRepository;
import com.tienda.repositories.PromocionRepository;
import com.tienda.repositories.specs.PedidoSpecs;
import com.tienda.services.IMovimientoStockService;
import com.tienda.services.IPedidoService;
import com.tienda.util.GeneradorTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tienda.dtos.pedido.PedidoResumenDTO;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements IPedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final PromocionRepository promocionRepository;
    private final PedidoMapper pedidoMapper;
    private final IMovimientoStockService movimientoStockService;
    private final GeneradorTicket generadorTicket;

    @Override
    public Page<PedidoResponse> obtenerPedidos(int pagina, int tamaño, String ordenarPor, String direccion,
                                               String nombreCliente, TipoVenta tipoVenta, EstadoPedido estadoPedido,
                                               LocalDate desde, LocalDate hasta){
        Sort.Direction sortDirection = "DESC".equalsIgnoreCase(direccion)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(sortDirection,ordenarPor);
        Pageable pageable = PageRequest.of(pagina,tamaño, sort);
        Specification<Pedido> specs = nombreCliente != null ?
                PedidoSpecs.porFiltros(sinAcentos(nombreCliente),estadoPedido,desde,hasta,tipoVenta) : PedidoSpecs.porFiltros(nombreCliente,estadoPedido,desde,hasta,tipoVenta);
        Page<Pedido> pedidos = pedidoRepository.findAll(specs,pageable);

        return pedidos.map(pedidoMapper::toResponse);
    }

    @Transactional
    @Override
    public PedidoResponse crearPedido(PedidoRequest request){
        Pedido pedido = inicializarPedido(request);

        double total = request.getProductos().stream()
                .mapToDouble(item -> procesarItem(pedido, item))
                .sum();

        pedido.setTotal(total);
        return pedidoMapper.toResponse(pedidoRepository.save(pedido));

    }

    public PedidoResponse obtenerPedidoPorId(Long id){
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("pedido no encontrado"));

        return pedidoMapper.toResponse(pedido);
    }

    @Transactional
    public PedidoResponse cambiarEstado(Long id, EstadoPedido estadoPedido){
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("pedido no encontrado"));

        // Validar tipo de venta del pedido
        if (pedido.getTipoVenta() == null) {
            throw new IllegalStateException("El pedido no tiene tipo de venta definido");
        }

        EstadoPedido estadoActual = pedido.getEstadoPedido();
        TipoVenta tipoVenta = pedido.getTipoVenta();

        // Validar transición de estado segun tipo de venta (local, envio)
        if (!estadoActual.puedeCambiarA(estadoPedido, tipoVenta)) {
            throw new IllegalStateException(
                    "No se puede cambiar el estado de "
                            + estadoActual + " a " + estadoPedido
                            + " para una venta " + tipoVenta
            );
        }

        // Si se cancela se devuelve al stock
        if(estadoPedido == EstadoPedido.CANCELADO){
            for(DetallePedido item : pedido.getItems()){
                if(item.getProducto() != null) {
                    movimientoStockService.entrada(
                            item.getProducto().getId(),
                            item.getCantidad(),
                            MotivoMovimiento.CANCELACION_PEDIDO
                    );
                }else if(item.getPromocion() != null){
                    for(PromocionProducto pp : item.getPromocion().getProductosDetalle()){
                        movimientoStockService.entrada(
                                pp.getProducto().getId(),
                                pp.getCantidad() * item.getCantidad(),
                                MotivoMovimiento.CANCELACION_PEDIDO
                        );
                    }
                }
            }
        }
        // Cambiar estado
        pedido.setEstadoPedido(estadoPedido);
        pedidoRepository.save(pedido);
        return pedidoMapper.toResponse(pedido);
    }

    public PedidoResumenDTO obtenerResumen() {
        PedidoResumenDTO dto = new PedidoResumenDTO();
        dto.setTotal(pedidoRepository.count());
        dto.setEn_preparacion(pedidoRepository.countByEstadoPedido(EstadoPedido.EN_PREPARACION));
        dto.setPendientes(pedidoRepository.countByEstadoPedido(EstadoPedido.PENDIENTE));
        dto.setEntregados(pedidoRepository.countByEstadoPedido(EstadoPedido.ENTREGADO));
        dto.setCancelados(pedidoRepository.countByEstadoPedido(EstadoPedido.CANCELADO));
        return dto;
    }

    private Pedido inicializarPedido(PedidoRequest request){
        Pedido pedido = new Pedido();
        pedido.setTelefono(request.getTelefono()); // <----- eliminar esto si el telefono es opcional en caso de venta en caja
        pedido.setMetodoPago(request.getMetodoPago());
        pedido.setTipoVenta(request.getTipoVenta());
        pedido.setFecha(LocalDateTime.now());

        if(request.getTipoVenta() == TipoVenta.ENVIO){
            pedido.setEstadoPedido(EstadoPedido.EN_PREPARACION);
            pedido.setDireccion(request.getDireccion());
            pedido.setCodigoPostal(request.getCodigoPostal());
            pedido.setLocalidad(request.getLocalidad());
            pedido.setCliente(request.getNombreCliente());
            pedido.setTelefono(request.getTelefono());
        } else {
            pedido.setEstadoPedido(EstadoPedido.PENDIENTE);
            pedido.setDireccion("-");
            pedido.setCodigoPostal("-");
            pedido.setLocalidad("-");
        }

        if(request.getTipoVenta() == TipoVenta.LOCAL){
            String nombreCliente = request.getNombreCliente().isEmpty() ? "Venta en Caja" : request.getNombreCliente();
            String telefonoCliente = request.getTelefono().isEmpty() ? "-" : request.getTelefono();

            pedido.setTelefono(telefonoCliente);
            pedido.setCliente(nombreCliente);
        }

        return pedido;
    }

    private double procesarItem(Pedido pedido, PedidoRequest.DetalleRequest item){
        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);
        double subTotal;

        if(item.getPromocionId() != null){
            subTotal = procesarPromocion(item, detalle);
        } else {
            subTotal = procesarProductoSimple(item, detalle);
        }

        detalle.setSubTotal(subTotal);
        pedido.addDetalle(detalle);
        return subTotal;
    }

    private double procesarPromocion(PedidoRequest.DetalleRequest item, DetallePedido detalle) {
        Promocion promo = promocionRepository.findByIdAndActivoTrue(item.getPromocionId());
        if (promo == null) throw new RuntimeException("Promoción no encontrada o inactiva");

        // Obtenemos la cantidad de combos que pidió el usuario (ej: 2 combos)
        int cantidadDeCombos = item.getCantidad();

        for(PromocionProducto pp : promo.getProductosDetalle()){
            int totalARestar = pp.getCantidad() * cantidadDeCombos;

            movimientoStockService.salida(
                    pp.getProducto().getId(),
                    totalARestar,
                    MotivoMovimiento.VENTA
            );
        }

        detalle.setPromocion(promo);
        detalle.setCantidad(cantidadDeCombos);
        return promo.getPrecioFinal() * cantidadDeCombos;
    }

    private double procesarProductoSimple(PedidoRequest.DetalleRequest item, DetallePedido detalle) {
        Producto producto = productoRepository.findById(item.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getProductId()));

        if (producto.getStock() < item.getCantidad()) {
            throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
        }

        movimientoStockService.salida(producto.getId(), item.getCantidad(), MotivoMovimiento.VENTA);

        detalle.setProducto(producto);
        detalle.setCantidad(item.getCantidad());
        detalle.setPrecioUnitario(producto.getPrecio());
        return producto.getPrecio() * item.getCantidad();
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generarTicketPDF(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (pedido.getEstadoPedido() == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("No se puede generar un ticket de un pedido que ha sido CANCELADO.");
        }

        return generadorTicket.generarTicket(pedido);
    }

    private String sinAcentos(String nombreBusqueda){
        return Normalizer.normalize(nombreBusqueda, Normalizer.Form.NFD)
                .replaceAll("\\p{InCOMBINING_DIACRITICAL_MARKS}+","");
    }
}

