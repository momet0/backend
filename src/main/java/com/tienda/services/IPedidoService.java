package com.tienda.services;

import com.tienda.dtos.pedido.PedidoRequest;
import com.tienda.dtos.pedido.PedidoResponse;
import com.tienda.dtos.pedido.PedidoResumenDTO;
import com.tienda.enums.EstadoPedido;
import com.tienda.enums.TipoVenta;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


public interface IPedidoService {

     Page<PedidoResponse> obtenerPedidos(int pagina, int tamaño, String ordenarPor, String direccion, String nombreCliente, TipoVenta tipoVenta, EstadoPedido estadoPedido, LocalDate desde, LocalDate hasta);

     PedidoResponse crearPedido(PedidoRequest pedido);

     PedidoResponse obtenerPedidoPorId(Long id);

     PedidoResponse cambiarEstado(Long id, EstadoPedido estadoPedido);

     PedidoResumenDTO obtenerResumen();

     @Transactional(readOnly = true)
     byte[] generarTicketPDF(Long pedidoId);
}
