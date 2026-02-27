package com.tienda.services.impl;

import com.tienda.dtos.CategoriaDTO;
import com.tienda.dtos.ProveedorDTO;
import com.tienda.dtos.marca.MarcaResponse;
import com.tienda.dtos.producto.*;
import com.tienda.enums.ActividadProducto;
import com.tienda.enums.EstadoStock;
import com.tienda.enums.TipoOperacionPrecio;
import com.tienda.mappers.ProductoDetalleMapper;
import com.tienda.mappers.ProductoMapper;
import com.tienda.models.Categoria;
import com.tienda.models.Marca;
import com.tienda.models.Producto;
import com.tienda.models.Proveedor;
import com.tienda.repositories.*;
import com.tienda.repositories.specs.ProductoSpecs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import com.tienda.enums.MotivoMovimiento;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private MovimientoStockRepository movimientoStockRepository;

    @Mock
    private ProductoDetalleMapper productoDetalleMapper;
    @Mock
    private MarcaRepository marcaRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProveedorRepository proveedorRepository;

    @Mock
    private MovimientoStockServiceImpl movimientoStockService;

    @Mock
    private ProductoMapper productoMapper;
    @Mock
    CloudinaryService cloudinaryService;

    private Producto producto;
    private Marca marca;
    private ProductoDetalleDTO productoDetalleDTO;
    private Categoria categoria;
    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Coca Cola");
        producto.setPrecio(1500.0);
        producto.setStock(100);
        producto.setMinStock(10);
        producto.setImagenUrl("asd");
        producto.setActivo(true);

        productoDetalleDTO = new ProductoDetalleDTO();
        productoDetalleDTO.setId(1L);
        productoDetalleDTO.setNombre("Coca Cola");
        productoDetalleDTO.setImagenUrl("asd");
        productoDetalleDTO.setPrecio(1500.0);
        productoDetalleDTO.setStock(100);

        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Bebidas");

        proveedor = new Proveedor();
        proveedor.setNombre("Juan");
        proveedor.setId(1L);

        marca = new Marca();
        marca.setId(1L);
        marca.setNombre("Coca");
    }

    @Test
    void obtenerProductosTest() {
        // Arrange
        int pagina = 0;
        int tamano = 10;
        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by(Sort.Direction.ASC, "id"));
        List<Producto> listaProductos = Collections.singletonList(producto);
        Page<Producto> pageProductos = new PageImpl<>(listaProductos);

        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(1L);
        productoDTO.setNombre("Coca Cola");

        when(productoRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(pageProductos);
        when(productoMapper.toDTO(any(Producto.class))).thenReturn(productoDTO);

        // Act
        Page<ProductoDTO> resultado = productoService.obtenerProductos(pagina, tamano,"id","ASC", ActividadProducto.ACTIVO,"",1L,1L, EstadoStock.ALTO, "marca");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(productoRepository).findAll(any(Specification.class), eq(pageable));
    }


    @Test
    void guardarProductoTest() {
        // Arrange
        ProductoDetalleDTO dto = new ProductoDetalleDTO();
        dto.setNombre("Coca Cola");
        dto.setPrecio(1500.0);
        dto.setStock(100);
        dto.setCategoria(new CategoriaDTO(1L, "Bebidas"));
        dto.setProveedor(new ProveedorDTO(1L, "Juan"));
        dto.setMarca(new MarcaResponse(1L, "Coca"));

        MultipartFile img = mock(MultipartFile.class);

        Producto productoSinDatos = new Producto();

        when(productoDetalleMapper.toEntity(dto)).thenReturn(productoSinDatos);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(marcaRepository.findByNombre("Coca")).thenReturn(Optional.of(marca));
        when(cloudinaryService.subirImagen(img)).thenReturn("url-imagen");
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));
        when(productoDetalleMapper.toDTO(any(Producto.class))).thenReturn(dto);

        // Act
        ProductoDetalleDTO resultado = productoService.guardarProducto(dto, img);

        // Assert
        assertNotNull(resultado);
        verify(categoriaRepository).findById(1L);
        verify(proveedorRepository).findById(1L);
        verify(marcaRepository).findByNombre("Coca");
        verify(cloudinaryService).subirImagen(img);

        verify(productoRepository).save(argThat(p ->
                p.getCategoria().equals(categoria) &&
                        p.getProveedor().equals(proveedor) &&
                        p.getMarca().equals(marca) &&
                        p.isActivo() &&
                        p.getImagenUrl().equals("url-imagen")
        ));
    }

    @Test
    void actualizarProductoExitosoTest() {
        // Arrange
        Long id = 1L;
        ProductoUpdateDTO dtoActualizar = new ProductoUpdateDTO();
        dtoActualizar.setNombre("Coca Cola Zero");
        dtoActualizar.setPrecio(1600.0);
        dtoActualizar.setStock(50);
        dtoActualizar.setActivo(true);
        MultipartFile img = mock(MultipartFile.class);
        when(productoRepository.findByIdWithCategoriaAndProveedor(id)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //when(productoDetalleMapper.toDTO(any(Producto.class))).thenReturn(dtoActualizar);

        // Act
        ProductoDetalleDTO resultado = productoService.actualizarProducto(id, dtoActualizar,img);

        // Assert
        assertNotNull(resultado);
        assertEquals("Coca Cola Zero", resultado.getNombre());
        verify(productoRepository).findByIdWithCategoriaAndProveedor(id);
        verify(productoRepository).save(producto);
    }

    @Test
    void actualizarProductoNoEncontradoTest() {
        // Arrange
        Long id = 99L;
        ProductoUpdateDTO dtoUpdate = new ProductoUpdateDTO();
        when(productoRepository.findByIdWithCategoriaAndProveedor(id)).thenReturn(Optional.empty());
        MultipartFile img = mock(MultipartFile.class);
        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            productoService.actualizarProducto(id, dtoUpdate,img);
        });

        assertTrue(exception.getMessage().contains("Producto no encontrado"));
        verify(productoRepository, never()).save(any());
    }

    @Test
    void borrarProductoTest() {
        Long id = 1L;

        when(productoRepository.findByIdWithCategoriaAndProveedor(id)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        productoService.suspenderProducto(id);

        verify(productoRepository).save(argThat(p -> !p.isActivo() && p.getId().equals(id)));
    }



    @Test
    void obtenerProductosConStockBajoTest() {
        // Arrange
        Producto prodBajoStock = new Producto();
        prodBajoStock.setId(2L);
        prodBajoStock.setNombre("Pan");
        prodBajoStock.setStock(5);
        prodBajoStock.setMinStock(10);

        Page<Producto> pageProductos = new PageImpl<>(List.of(prodBajoStock));

        ProductoDetalleDTO dtoBajoStock = new ProductoDetalleDTO();
        dtoBajoStock.setNombre("Pan");
        Specification<Producto> specs = ProductoSpecs.porFiltros(ActividadProducto.ACTIVO,"Pan",1L,1L,EstadoStock.ALTO, "marca");

        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setNombre("Pan");

        when(productoRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(pageProductos);
        when(productoMapper.toDTO(any(Producto.class))).thenReturn(productoDTO);

        // Act
        Page<ProductoDTO> resultado = productoService.obtenerProductos(0, 10,"id","ASC",ActividadProducto.ACTIVO,"",1L,1L,EstadoStock.ALTO, "marca");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Pan", resultado.getContent().get(0).getNombre());
    }

    @Test
    void obtenerProductoPorIdNoEncontradoTest() {
        // Arrange
        Long id = 99L;
        when(productoRepository.findByIdWithCategoriaAndProveedor(id)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            productoService.obtenerProductoPorId(id);
        });
    }

    @Test
    void findByCategoriaTest() {
        // Arrange
        Long catId = 5L;
        List<Producto> lista = Collections.singletonList(producto);
        List<ProductoDetalleDTO> listaDto = Collections.singletonList(productoDetalleDTO);

        when(productoRepository.findByCategoriaId(catId)).thenReturn(lista);
        when(productoDetalleMapper.toDTO(any(Producto.class))).thenReturn(productoDetalleDTO);
        // Act
        List<ProductoDetalleDTO> resultado = productoService.findByCategoria(catId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(productoRepository).findByCategoriaId(catId);
    }

    @Test
    void obtenerResumen() {
        // arrange
        when(productoRepository.count()).thenReturn(10L);
        when(productoRepository.countByStockAndActivoTrue(0)).thenReturn(2L);
        when(productoRepository.countProductosConStockBajo()).thenReturn(1L);

        // act
        ProductoResumenDTO resumen = productoService.obtenerResumen();

        // assert
        assertEquals(10L, resumen.getTotal());
        assertEquals(2L, resumen.getSinStock());
        assertEquals(1L, resumen.getStockBajo());
    }
/*
    @Test
    void actualizarProducto_deInactivoAActivo_registraMovimientoActivacion() {
        // Arrange
        producto.setActivo(false);
        producto.setStock(0);

        ProductoUpdateDTO dto = new ProductoUpdateDTO();
        dto.setActivo(true);
        dto.setStock(50);
        MultipartFile img = mock(MultipartFile.class);
        when(productoRepository.findByIdWithCategoriaAndProveedor(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(productoDetalleMapper.toDTO(any(Producto.class)))
                .thenReturn(productoDetalleDTO);

        // Act
        productoService.actualizarProducto(1L, dto,img);

        // Assert
        verify(movimientoStockRepository).save(
                argThat(mov ->
                        mov.getMotivo() == MotivoMovimiento.ACTIVACION_PRODUCTO &&
                                mov.getProducto().equals(producto) && mov.getStockAnterior() == 0 && mov.getStockActual() == 50
                )
        );
    }
*/
    @Test
    void actualizarProducto_activoAActivo_noRegistraMovimiento() {
        // Arrange
        producto.setActivo(true);

        ProductoUpdateDTO dto = new ProductoUpdateDTO();
        dto.setActivo(true);
        MultipartFile img = mock(MultipartFile.class);
        dto.setNombre("Mismo nombre");

        when(productoRepository.findByIdWithCategoriaAndProveedor(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(productoDetalleMapper.toDTO(any(Producto.class)))
                .thenReturn(productoDetalleDTO);

        // Act
        productoService.actualizarProducto(1L, dto, img);

        // Assert
        verify(movimientoStockRepository, never()).save(any());
        verify(movimientoStockService, never()).ajuste(any(), anyInt(), any());
    }

    @Test
    void guardarProducto_CategoriaNoEncontrada_LanzaExcepcion() {
        // Arrange
        ProductoDetalleDTO dto = new ProductoDetalleDTO();
        dto.setNombre("Coca Cola");
        dto.setPrecio(1500.0);
        dto.setStock(100);
        dto.setCategoria(new CategoriaDTO(99L, "Inexistente")); // ID que no existe

        MultipartFile img = mock(MultipartFile.class);

        when(productoDetalleMapper.toEntity(dto)).thenReturn(new Producto());
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                productoService.guardarProducto(dto, img)
        );

        assertEquals("Categoría no encontrada", ex.getMessage());
        verify(productoRepository, never()).save(any());
    }

    @Test
    void modificarPrecios_porMarca_aumentaPreciosCorrectamente() {
        ModificarPrecioRequest request = new ModificarPrecioRequest();
        request.setPorcentaje(10);
        request.setTipoOperacion(TipoOperacionPrecio.AUMENTO);
        request.setMarcaId(1L);

        Producto producto = new Producto();
        producto.setPrecio(1000.0);
        producto.setActivo(true);

        when(productoRepository.findByMarcaIdAndActivoTrue(1L))
                .thenReturn(List.of(producto));

        productoService.modificarPrecios(request);

        assertEquals(1100.0, producto.getPrecio());
        verify(productoRepository).save(producto);
    }

    @Test
    void modificarPrecios_porMarca_descuentoPreciosCorrectamente() {
        ModificarPrecioRequest request = new ModificarPrecioRequest();
        request.setPorcentaje(20);
        request.setTipoOperacion(TipoOperacionPrecio.DESCUENTO);
        request.setMarcaId(1L);

        Producto producto = new Producto();
        producto.setPrecio(1000.0);
        producto.setActivo(true);

        when(productoRepository.findByMarcaIdAndActivoTrue(1L))
                .thenReturn(List.of(producto));

        productoService.modificarPrecios(request);

        assertEquals(800.0, producto.getPrecio());
        verify(productoRepository).save(producto);
    }

    @Test
    void modificarPrecios_ambosFiltros_lanzaExcepcion() {
        ModificarPrecioRequest request = new ModificarPrecioRequest();
        request.setPorcentaje(10);
        request.setTipoOperacion(TipoOperacionPrecio.AUMENTO);
        request.setMarcaId(1L);
        request.setProveedorId(1L);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                productoService.modificarPrecios(request)
        );

        assertEquals("Seleccionar solo proveedor o marca", ex.getMessage());
    }
}
