package com.tienda.services.impl;

import com.tienda.dtos.CategoriaDTO;
import com.tienda.mappers.CategoriaMapper;
import com.tienda.models.Categoria;
import com.tienda.repositories.CategoriaRepository;
import com.tienda.services.ICategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements ICategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    public List<CategoriaDTO> obtenerCategorias() {
        return categoriaMapper.toDTOList(categoriaRepository.findAll());
    }

    @Override
    public CategoriaDTO obtenerCategoriaPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre)
                .map(categoriaMapper::toDTO)
                .orElse(null);
    }

    @Override
    public CategoriaDTO obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id)
                .map(categoriaMapper::toDTO)
                .orElse(null);
    }

    @Override
    public CategoriaDTO crearCategoria(CategoriaDTO categoria) {
        Categoria categoriaEntity = categoriaMapper.toEntity(categoria);
        return categoriaMapper.toDTO(categoriaRepository.save(categoriaEntity));
    }

    @Override
    public CategoriaDTO actualizarCategoria(Long id, CategoriaDTO categoria) {
        Optional<Categoria> categoriaExistente = categoriaRepository.findById(id);

        if (categoriaExistente.isPresent()) {
            categoriaExistente.get().setNombre(categoria.getNombre());
            return categoriaMapper.toDTO(categoriaRepository.save(categoriaExistente.get()));
        }
        return null;
    }

    @Override
    public void eliminarCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }
}
