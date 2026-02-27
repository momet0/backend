package com.tienda.services;

import com.tienda.dtos.ProveedorDTO;
import com.tienda.models.Proveedor;

import java.util.Collection;
import java.util.List;

public interface IProveedorService {
    List<ProveedorDTO> listarProveedores();
    List<ProveedorDTO> listarProveedorPorNombre(String nombre);
    List<ProveedorDTO> listarProveedorPorListaDeProductos(Collection<String> nombresProductos);
    ProveedorDTO crearProveedor(ProveedorDTO proveedor);
    ProveedorDTO actualizarProveedor(Long id, ProveedorDTO proveedor);
    void borrarProveedor(Long id);
}
