package com.tienda.services.impl;

import com.tienda.dtos.pedido.PedidoRequest;
import com.tienda.dtos.pedido.PedidoResponse;
import com.tienda.dtos.pedido.PedidoResumenDTO;
import com.tienda.enums.EstadoPedido;
import com.tienda.enums.MetodoPago;
import com.tienda.enums.MotivoMovimiento;
import com.tienda.enums.TipoVenta;
import com.tienda.mappers.PedidoMapper;
import com.tienda.models.DetallePedido;
import com.tienda.models.Pedido;
import com.tienda.models.Producto;
import com.tienda.repositories.PedidoRepository;
import com.tienda.repositories.ProductoRepository;
import com.tienda.services.IMovimientoStockService;
import com.tienda.util.GeneradorTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private PedidoMapper pedidoMapper;

    @Mock
    private IMovimientoStockService movimientoStockService;

    @Mock
    private GeneradorTicket generadorTicket;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private Pedido pedido;
    private PedidoResponse pedidoResponse;
    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Test");
        producto.setPrecio(100.0);
        producto.setStock(50);

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCliente("Juan Perez");
        pedido.setEstadoPedido(EstadoPedido.PENDIENTE);
        pedido.setFecha(LocalDateTime.now());
        pedido.setItems(new ArrayList<>());

        pedidoResponse = new PedidoResponse();
        pedidoResponse.setId(1L);
        pedidoResponse.setNombreCliente("Juan Perez");
        pedidoResponse.setEstado(EstadoPedido.PENDIENTE);
    }

    @Test
    void obtenerPedidosTest() {
        int pagina = 0;
        int tamano = 10;

        when(pedidoRepository.findAll(
                any(Specification.class),
                any(Pageable.class)
        )).thenReturn(new PageImpl<>(Collections.singletonList(pedido)));

        when(pedidoMapper.toResponse(any(Pedido.class)))
                .thenReturn(pedidoResponse);

        Page<PedidoResponse> resultado = pedidoService.obtenerPedidos(
                pagina,
                tamano,
                "fecha",
                "DESC",
                null,
                null,
                null,
                null,
                null
        );

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());

        verify(pedidoRepository).findAll(
                any(Specification.class),
                any(Pageable.class)
        );
    }

    @Test
    void crearPedidoExitosoTest() {
        // Arrange
        PedidoRequest request = new PedidoRequest();
        request.setNombreCliente("Juan Perez");
        request.setMetodoPago(MetodoPago.EFECTIVO);
        request.setTipoVenta(TipoVenta.LOCAL);

        PedidoRequest.DetalleRequest detalleReq = new PedidoRequest.DetalleRequest();
        detalleReq.setProductId(1L);
        detalleReq.setCantidad(2);
        request.setProductos(Collections.singletonList(detalleReq));

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(pedidoMapper.toResponse(any(Pedido.class))).thenReturn(pedidoResponse);

        // Act
        PedidoResponse resultado = pedidoService.crearPedido(request);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan Perez", resultado.getNombreCliente());
        verify(movimientoStockService).salida(eq(1L), eq(2), eq(MotivoMovimiento.VENTA));
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void crearPedidoConEnvioTest() {
        // Arrange
        PedidoRequest request = new PedidoRequest();
        request.setNombreCliente("Maria Gomez");
        request.setTipoVenta(TipoVenta.ENVIO);
        request.setDireccion("Calle Falsa 123");
        request.setCodigoPostal("1234");
        request.setLocalidad("CABA");
        request.setProductos(Collections.emptyList()); // Sin productos para simplificar este caso

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(pedidoMapper.toResponse(any(Pedido.class))).thenReturn(pedidoResponse);

        // Act
        PedidoResponse resultado = pedidoService.crearPedido(request);

        // Assert
        assertNotNull(resultado);
        verify(pedidoRepository).save(argThat( p ->
                p.getEstadoPedido() == EstadoPedido.EN_PREPARACION &&
                p.getDireccion().equals("Calle Falsa 123")));
    }

    @Test
    void crearPedidoSinStockTest() {
        // Arrange
        PedidoRequest request = new PedidoRequest();
        request.setTipoVenta(TipoVenta.LOCAL);

        PedidoRequest.DetalleRequest detalleReq = new PedidoRequest.DetalleRequest();
        detalleReq.setProductId(1L);
        detalleReq.setCantidad(100); // Stock es 50
        request.setProductos(Collections.singletonList(detalleReq));

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.crearPedido(request);
        });

        String mensajeEsperado = "Stock insuficiente para: " + producto.getNombre();
        assertEquals(mensajeEsperado, exception.getMessage());

        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void obtenerPedidoPorIdTest() {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoMapper.toResponse(pedido)).thenReturn(pedidoResponse);

        // Act
        PedidoResponse resultado = pedidoService.obtenerPedidoPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void cambiarEstadoACanceladoLocalTest() {
        // Arrange
        pedido.setTipoVenta(TipoVenta.LOCAL);
        pedido.setEstadoPedido(EstadoPedido.PENDIENTE);

        DetallePedido detalle = new DetallePedido();
        detalle.setProducto(producto);
        detalle.setCantidad(5);
        pedido.addDetalle(detalle);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArguments()[0]);
        when(pedidoMapper.toResponse(any(Pedido.class))).thenReturn(pedidoResponse);

        // Act
        pedidoService.cambiarEstado(1L, EstadoPedido.CANCELADO);

        // Assert
        assertEquals(EstadoPedido.CANCELADO, pedido.getEstadoPedido());
        verify(movimientoStockService).entrada(eq(1L), eq(5), eq(MotivoMovimiento.CANCELACION_PEDIDO));
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void cambiarEstadoACanceladoEnvioTest() {
        pedido.setTipoVenta(TipoVenta.ENVIO);
        pedido.setEstadoPedido(EstadoPedido.PENDIENTE);

        DetallePedido detalle = new DetallePedido();
        detalle.setProducto(producto);
        detalle.setCantidad(3);
        pedido.addDetalle(detalle);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArguments()[0]);
        when(pedidoMapper.toResponse(any(Pedido.class))).thenReturn(pedidoResponse);

        pedidoService.cambiarEstado(1L, EstadoPedido.CANCELADO);

        assertEquals(EstadoPedido.CANCELADO, pedido.getEstadoPedido());
    }

    @Test
    void cambiarEstadoYaCanceladoTest() {
        // Arrange
        pedido.setEstadoPedido(EstadoPedido.CANCELADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            pedidoService.cambiarEstado(1L, EstadoPedido.ENTREGADO);
        });
    }

    @Test
    void obtenerResumen() {
        // arrange
        when(pedidoRepository.count()).thenReturn(20L);
        when(pedidoRepository.countByEstadoPedido(EstadoPedido.EN_PREPARACION)).thenReturn(6L);
        when(pedidoRepository.countByEstadoPedido(EstadoPedido.PENDIENTE)).thenReturn(5L);
        when(pedidoRepository.countByEstadoPedido(EstadoPedido.ENTREGADO)).thenReturn(6L);
        when(pedidoRepository.countByEstadoPedido(EstadoPedido.CANCELADO)).thenReturn(3L);

        // act
        PedidoResumenDTO resumen = pedidoService.obtenerResumen();

        // assert
        assertEquals(20, resumen.getTotal());
        assertEquals(6, resumen.getEn_preparacion());
        assertEquals(5, resumen.getPendientes());
        assertEquals(6, resumen.getEntregados());
        assertEquals(3, resumen.getCancelados());
    }

    @Test
    void generarTicketPDFTest() {
        // Arrange
        Long pedidoId = 1L;
        byte[] pdfEsperado = "Contenido PDF de prueba".getBytes();

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(generadorTicket.generarTicket(pedido)).thenReturn(pdfEsperado);

        // Act
        byte[] resultado = pedidoService.generarTicketPDF(pedidoId);

        // Assert
        assertNotNull(resultado);
        assertArrayEquals(pdfEsperado, resultado);

        // Verificamos que se llamó al repositorio y al generador una vez
        verify(pedidoRepository).findById(pedidoId);
        verify(generadorTicket).generarTicket(pedido);
    }

    @Test
    void generarTicketPDFPedidoNoEncontradoTest() {
        // Arrange
        Long pedidoId = 99L;
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.generarTicketPDF(pedidoId);
        });

        assertEquals("Pedido no encontrado", exception.getMessage());
        verify(generadorTicket, never()).generarTicket(any());
    }
}
