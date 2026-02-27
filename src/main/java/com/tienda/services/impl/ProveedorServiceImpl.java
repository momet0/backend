package com.tienda.services.impl;

import com.tienda.dtos.ProveedorDTO;
import com.tienda.mappers.ProveedorMapper;
import com.tienda.models.Proveedor;
import com.tienda.repositories.ProveedorRepository;
import com.tienda.services.IProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements IProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDTO> listarProveedores() {
        return proveedorMapper.toDTOList(proveedorRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDTO> listarProveedorPorNombre(String nombre) {
        return proveedorMapper.toDTOList(proveedorRepository.findByNombreContainingIgnoreCase(nombre));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDTO> listarProveedorPorListaDeProductos(Collection<String> nombresProductos) {
        return proveedorMapper.toDTOList(proveedorRepository.findDistinctByListaProductosNombreIn(nombresProductos));
    }

    @Override
    @Transactional
    public ProveedorDTO crearProveedor(ProveedorDTO proveedorDto) {
        Proveedor prov = proveedorMapper.toEntity(proveedorDto);
        return proveedorMapper.toDTO(proveedorRepository.save(prov));
    }

    @Override
    @Transactional
    public ProveedorDTO actualizarProveedor(Long id, ProveedorDTO proveedorDto) {
        return proveedorRepository.findById(id).map(p -> {
            p.setNombre(proveedorDto.getNombre());
            return proveedorMapper.toDTO(proveedorRepository.save(p));
        }).orElseThrow( () -> new RuntimeException("Proveedor no encontrado"));
    }

    @Override
    public void borrarProveedor(Long id) {
        proveedorRepository.deleteById(id);
    }
}
