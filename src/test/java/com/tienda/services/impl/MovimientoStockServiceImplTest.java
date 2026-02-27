package com.tienda.services.impl;

import com.tienda.dtos.MovimientoStockDTO;
import com.tienda.enums.MotivoMovimiento;
import com.tienda.enums.TipoCambio;
import com.tienda.mappers.MovimientoStockMapper;
import com.tienda.models.MovimientoStock;
import com.tienda.models.Producto;
import com.tienda.repositories.MovimientoStockRepository;
import com.tienda.repositories.ProductoRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientoStockServiceImplTest {

    @Mock
    private MovimientoStockRepository movimientoStockRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private MovimientoStockMapper movimientoStockMapper;

    @InjectMocks
    private MovimientoStockServiceImpl movimientoStockService;

    private Producto producto;
    private MovimientoStock movimientoEntidad;
    private MovimientoStockDTO movimientoDTO;

    @BeforeEach
    void setUp() {
        // Entidad Producto base
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Test");
        producto.setStock(10);

        // Entidad Movimiento con los nuevos nombres de campos
        movimientoEntidad = new MovimientoStock();
        movimientoEntidad.setId(1L);
        movimientoEntidad.setValorNumerico(5); // Antes setCantidad
        movimientoEntidad.setValorAnterior(10.0);
        movimientoEntidad.setValorActual(15.0);
        movimientoEntidad.setMotivo(MotivoMovimiento.REABASTECIMIENTO);
        movimientoEntidad.setProducto(producto);

        // DTO para retornos
        movimientoDTO = new MovimientoStockDTO();
        movimientoDTO.setId(1L);
    }

    @Test
    void entradaAumentaStockYRegistraMovimiento() {
        // Arrange
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(movimientoStockRepository.save(any(MovimientoStock.class))).thenReturn(movimientoEntidad);
        when(movimientoStockMapper.toDTO(any(MovimientoStock.class))).thenReturn(movimientoDTO);

        // Act
        movimientoStockService.entrada(1L, 5, MotivoMovimiento.REABASTECIMIENTO);

        // Assert
        assertEquals(15, producto.getStock());
        verify(productoRepository).save(producto);
        // Verificamos que se guardó con el nuevo campo valorNumerico
        verify(movimientoStockRepository).save(argThat(mov -> mov.getValorNumerico() == 5));
    }

    @Test
    void salidaReduceStockYRegistraMovimiento() {
        // Arrange
        lenient().when(productoRepository.findByIdWithCategoriaAndProveedor(anyLong())).thenReturn(Optional.of(producto));
        lenient().when(productoRepository.findById(anyLong())).thenReturn(Optional.of(producto));
        when(movimientoStockRepository.save(any(MovimientoStock.class))).thenReturn(movimientoEntidad);
        when(movimientoStockMapper.toDTO(any(MovimientoStock.class))).thenReturn(movimientoDTO);

        // Act
        movimientoStockService.salida(1L, 4, MotivoMovimiento.VENTA);

        // Assert
        assertEquals(6, producto.getStock());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void salidaSinStockLanzaExcepcion() {
        // Arrange
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                movimientoStockService.salida(1L, 20, MotivoMovimiento.VENTA)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("stock insuficiente"));
    }

    @Test
    void ajusteActualizaStockYRegistraMovimiento() {
        // Arrange
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(movimientoStockRepository.save(any(MovimientoStock.class))).thenReturn(movimientoEntidad);
        when(movimientoStockMapper.toDTO(any(MovimientoStock.class))).thenReturn(movimientoDTO);

        // Act
        movimientoStockService.ajuste(1L, 25, MotivoMovimiento.AJUSTE_STOCK);

        // Assert
        assertEquals(25, producto.getStock());
        verify(productoRepository).save(producto);
    }

    @Test
    void obtenerPorIdTest() {
        // Arrange
        when(movimientoStockRepository.findById(1L)).thenReturn(Optional.of(movimientoEntidad));
        when(movimientoStockMapper.toDTO(any(MovimientoStock.class))).thenReturn(movimientoDTO);

        // Act
        MovimientoStockDTO resultado = movimientoStockService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void historialPorProductoTest() {
        // Arrange
        when(movimientoStockRepository.findByProductoIdOrderByFechaDesc(1L))
                .thenReturn(List.of(movimientoEntidad));

        // Act
        List<MovimientoStock> resultado = movimientoStockService.historialPorProducto(1L);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(MotivoMovimiento.REABASTECIMIENTO, resultado.get(0).getMotivo());
    }

    @Test
    void obtenerTodos_DebeUsarFiltrosOpcionales() {
        // Arrange
        Page<MovimientoStock> page = new PageImpl<>(List.of(movimientoEntidad));
        when(movimientoStockRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);
        when(movimientoStockMapper.toDTO(any(MovimientoStock.class))).thenReturn(movimientoDTO);

        // Act
        Page<MovimientoStockDTO> resultado = movimientoStockService.obtenerTodos(
                0, 10, "id", "DESC",  TipoCambio.STOCK, null, null, null, null,null, null, null, null);

        // Assert
        assertNotNull(resultado);
        verify(movimientoStockRepository).findAll(any(Specification.class), any(Pageable.class));
    }
}