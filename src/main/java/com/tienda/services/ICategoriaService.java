package com.tienda.services;

import com.tienda.dtos.CategoriaDTO;
import com.tienda.models.Categoria;
import java.util.List;
import java.util.Optional;

public interface ICategoriaService {
    List<CategoriaDTO> obtenerCategorias();
    CategoriaDTO obtenerCategoriaPorNombre(String nombre);

    CategoriaDTO obtenerCategoriaPorId(Long id);
    CategoriaDTO crearCategoria(CategoriaDTO categoria);
    CategoriaDTO actualizarCategoria(Long id, CategoriaDTO categoria);
    void eliminarCategoria(Long id);
}
