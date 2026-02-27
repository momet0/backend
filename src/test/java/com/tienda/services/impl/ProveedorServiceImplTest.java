package com.tienda.services.impl;

import com.tienda.dtos.ProveedorDTO;
import com.tienda.mappers.ProveedorMapper;
import com.tienda.models.Proveedor;
import com.tienda.repositories.ProveedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProveedorServiceImplTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @Mock
    private ProveedorMapper proveedorMapper;

    @InjectMocks
    private ProveedorServiceImpl proveedorService;

    private Proveedor proveedor;
    private ProveedorDTO proveedorDTO;

    @BeforeEach
    void setUp() {
        proveedor = new Proveedor();
        proveedor.setId(1L);
        proveedor.setNombre("Ricardo Milos");
        //proveedor.setContacto("ricardit@test.com");

        proveedorDTO = new ProveedorDTO();
        proveedorDTO.setId(1L);
        proveedorDTO.setNombre("Ricardo Milos");
    }

    @Test
    void listarProveedoresTest() {
        // Arrange
        when(proveedorRepository.findAll()).thenReturn(List.of(proveedor));
        when(proveedorMapper.toDTOList(any())).thenReturn(List.of(proveedorDTO));

        // Act
        List<ProveedorDTO> resultado = proveedorService.listarProveedores();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(proveedorRepository).findAll();
        verify(proveedorMapper).toDTOList(any());
    }

    @Test
    void listarProveedorPorNombreTest() {
        // Arrange
        String nombre = "Test";
        when(proveedorRepository.findByNombreContainingIgnoreCase(nombre))
                .thenReturn(List.of(proveedor));
        when(proveedorMapper.toDTOList(any()))
                .thenReturn(List.of(proveedorDTO));

        // Act
        List<ProveedorDTO> resultado =
                proveedorService.listarProveedorPorNombre(nombre);

        // Assert
        assertEquals(1, resultado.size());
        verify(proveedorRepository)
                .findByNombreContainingIgnoreCase(nombre);
    }

    @Test
    void listarProveedorPorListaDeProductosTest() {
        // Arrange
        Collection<String> productos = Set.of("Shampoo", "Mascara");

        when(proveedorRepository.findDistinctByListaProductosNombreIn(productos))
                .thenReturn(List.of(proveedor));
        when(proveedorMapper.toDTOList(any()))
                .thenReturn(List.of(proveedorDTO));

        // Act
        List<ProveedorDTO> resultado =
                proveedorService.listarProveedorPorListaDeProductos(productos);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(proveedorRepository)
                .findDistinctByListaProductosNombreIn(productos);
    }

    @Test
    void crearProveedorTest() {
        // Arrange
        when(proveedorMapper.toEntity(proveedorDTO)).thenReturn(proveedor);
        when(proveedorRepository.save(any(Proveedor.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        when(proveedorMapper.toDTO(any(Proveedor.class)))
                .thenReturn(proveedorDTO);

        // Act
        ProveedorDTO resultado =
                proveedorService.crearProveedor(proveedorDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("Ricardo Milos", resultado.getNombre());
        verify(proveedorRepository).save(any(Proveedor.class));
    }

    @Test
    void actualizarProveedorExitosoTest() {
        // Arrange
        when(proveedorRepository.findById(1L))
                .thenReturn(Optional.of(proveedor));
        when(proveedorRepository.save(any(Proveedor.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        when(proveedorMapper.toDTO(any(Proveedor.class)))
                .thenReturn(proveedorDTO);

        // Act
        ProveedorDTO resultado =
                proveedorService.actualizarProveedor(1L, proveedorDTO);

        // Assert
        assertEquals("Ricardo Milos", resultado.getNombre());
        verify(proveedorRepository).save(proveedor);
    }

    @Test
    void actualizarProveedorNoEncontradoTest() {
        // Arrange
        when(proveedorRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> proveedorService.actualizarProveedor(1L, proveedorDTO)
        );

        assertTrue(exception.getMessage().contains("Proveedor no encontrado"));
        verify(proveedorRepository, never()).save(any());
    }

    @Test
    void borrarProveedorTest() {
        // Act
        proveedorService.borrarProveedor(1L);

        // Assert
        verify(proveedorRepository).deleteById(1L);
    }
}
