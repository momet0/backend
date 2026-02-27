package com.tienda.services.impl;

import com.tienda.dtos.CategoriaDTO;
import com.tienda.mappers.CategoriaMapper;
import com.tienda.repositories.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import com.tienda.models.Categoria;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private CategoriaMapper categoriaMapper;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    private Categoria categoria;
    private CategoriaDTO categoriaDTO;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Bebidas");

        categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(1L);
        categoriaDTO.setNombre("Bebidas");
    }

    @Test
    void obtenerCategoriasTest() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));
        when(categoriaMapper.toDTOList(any())).thenReturn(List.of(categoriaDTO));

        List<CategoriaDTO> resultado = categoriaService.obtenerCategorias();

        assertEquals(1, resultado.size());
        assertEquals("Bebidas", resultado.get(0).getNombre());
        verify(categoriaRepository).findAll();
    }

    @Test
    void obtenerCategoriaPorNombreTest() {
        when(categoriaRepository.findByNombre("Bebidas"))
                .thenReturn(Optional.of(categoria));
        when(categoriaMapper.toDTO(categoria)).thenReturn(categoriaDTO);

        CategoriaDTO resultado = categoriaService.obtenerCategoriaPorNombre("Bebidas");

        assertNotNull(resultado);
        assertEquals("Bebidas", resultado.getNombre());
    }

    @Test
    void obtenerCategoriaPorIdTest() {
        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));
        when(categoriaMapper.toDTO(categoria)).thenReturn(categoriaDTO);

        CategoriaDTO resultado = categoriaService.obtenerCategoriaPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void crearCategoriaTest() {
        when(categoriaMapper.toEntity(any(CategoriaDTO.class))).thenReturn(categoria);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);
        when(categoriaMapper.toDTO(any(Categoria.class))).thenReturn(categoriaDTO);

        CategoriaDTO resultado = categoriaService.crearCategoria(categoriaDTO);

        assertNotNull(resultado);
        assertEquals("Bebidas", resultado.getNombre());
        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    void actualizarCategoriaTest() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        CategoriaDTO dtoModificado = new CategoriaDTO();
        dtoModificado.setNombre("Lacteos");

        CategoriaDTO dtoResultado = new CategoriaDTO();
        dtoResultado.setId(1L);
        dtoResultado.setNombre("Lacteos");
        when(categoriaMapper.toDTO(any(Categoria.class))).thenReturn(dtoResultado);
        CategoriaDTO resultado = categoriaService.actualizarCategoria(1L, dtoModificado);

        assertNotNull(resultado);
        assertEquals("Lacteos", resultado.getNombre());
        verify(categoriaRepository).save(categoria);


    }

    @Test
    void eliminarCategoriaTest() {
        categoriaService.eliminarCategoria(1L);

        verify(categoriaRepository).deleteById(1L);
    }
}

