package com.tienda.services;

import com.tienda.dtos.MovimientoStockDTO;
import com.tienda.enums.MotivoMovimiento;
import com.tienda.enums.TipoCambio;
import com.tienda.models.MovimientoStock;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface IMovimientoStockService {

    List<MovimientoStock> historialPorProducto(Long productoId);

    Page<MovimientoStockDTO> obtenerTodos(
            int pagina,
            int tamaño,
            String ordenarPor,
            String direccion,
            TipoCambio tipoCambio,
            MotivoMovimiento motivo,
            Long productoId,
            Long marcaId,
            Long proveedorId,
            LocalDate inicio,
            LocalDate fin,
            String nombreProducto,
            String nombreUsuario);

    MovimientoStockDTO entrada(Long productoId, int cantidad, MotivoMovimiento motivo);
    MovimientoStockDTO salida(Long productoId, int cantidad, MotivoMovimiento motivo);
    MovimientoStockDTO ajuste(Long productoId, int nuevoStock, MotivoMovimiento motivo);
    MovimientoStockDTO obtenerPorId(Long id);
}
