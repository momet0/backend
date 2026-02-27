package com.tienda.services.impl;

import com.tienda.dtos.MovimientoStockDTO;
import com.tienda.enums.MotivoMovimiento;
import com.tienda.enums.TipoCambio;
import com.tienda.mappers.MovimientoStockMapper;
import com.tienda.models.Marca;
import com.tienda.models.Producto;
import com.tienda.models.User;
import com.tienda.repositories.MarcaRepository;
import com.tienda.repositories.MovimientoStockRepository;
import com.tienda.repositories.ProductoRepository;
import com.tienda.repositories.ProveedorRepository;
import com.tienda.repositories.specs.MovimientoStockSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.tienda.models.MovimientoStock;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.tienda.services.IMovimientoStockService;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovimientoStockServiceImpl implements IMovimientoStockService {

    private final ProductoRepository productoRepo;
    private final MovimientoStockRepository movimientoStockRepository;
    private final MovimientoStockMapper movimientoStockMapper;

    @Override
    public List<MovimientoStock> historialPorProducto(Long id) {
        return movimientoStockRepository.findByProductoIdOrderByFechaDesc(id);
    }

    @Override
    public Page<MovimientoStockDTO> obtenerTodos(int pagina, int tamaño, String ordenarPor, String direccion,
                                                 TipoCambio tipoCambio, MotivoMovimiento motivo, Long productoId, Long marcaId, Long proveedorId,
                                                 LocalDate inicio, LocalDate fin,
                                                 String nombreProducto, String nombreUsuario) {
        Sort.Direction sortDirection = "DESC".equalsIgnoreCase(direccion) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(pagina, tamaño, Sort.by(sortDirection, ordenarPor));

        // Normalizar inputs para búsqueda sin tildes
        String nombreProductoNorm = normalizar(nombreProducto);
        String nombreUsuarioNorm = normalizar(nombreUsuario);

        Specification<MovimientoStock> specs = MovimientoStockSpecs.porFiltros(tipoCambio,
                motivo, productoId, marcaId, proveedorId, inicio, fin, nombreProductoNorm, nombreUsuarioNorm
        );
        Page<MovimientoStock> movimientos = movimientoStockRepository.findAll(specs, pageable);
        return movimientos.map(movimientoStockMapper::toDTO);
    }

    @Transactional
    @Override
    public MovimientoStockDTO entrada(Long productoId, int cantidad, MotivoMovimiento motivo) {
        Producto p = buscarProducto(productoId);

        int stockAnterior = p.getStock();
        int stockActual = stockAnterior + cantidad;

        p.setStock(p.getStock() + cantidad);
        productoRepo.save(p);

        MovimientoStock mov = new MovimientoStock();
        mov.setTipoCambio(TipoCambio.STOCK);
        mov.setProducto(p);
        mov.setValorNumerico(cantidad);
        mov.setValorAnterior((double) stockAnterior);
        mov.setValorActual((double) stockActual);
        mov.setMotivo(motivo);
        mov.setFecha(LocalDateTime.now());
        setUsuario(mov);

        return movimientoStockMapper.toDTO(movimientoStockRepository.save(mov));
    }

    @Transactional
    @Override
    public MovimientoStockDTO salida(Long productoId, int cantidad, MotivoMovimiento motivo) {
        Producto p = buscarProducto(productoId);
        if (p.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        int stockAnterior = p.getStock();
        int stockActual = stockAnterior - cantidad;

        p.setStock(p.getStock() - cantidad);
        productoRepo.save(p);

        MovimientoStock mov = new MovimientoStock();
        mov.setTipoCambio(TipoCambio.STOCK);
        mov.setProducto(p);
        mov.setValorNumerico(-cantidad);
        mov.setValorAnterior((double) stockAnterior);
        mov.setValorActual((double) stockActual);
        mov.setMotivo(motivo);
        mov.setFecha(LocalDateTime.now());
        setUsuario(mov);


        return movimientoStockMapper.toDTO(movimientoStockRepository.save(mov));
    }

    @Override
    public MovimientoStockDTO obtenerPorId(Long id){
        MovimientoStock movimiento = movimientoStockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        return movimientoStockMapper.toDTO(movimiento);
    }

    @Transactional
    @Override
    public MovimientoStockDTO ajuste(Long productoId, int nuevoStock, MotivoMovimiento motivo) {
        Producto p = buscarProducto(productoId);
        if (nuevoStock < 0) throw new RuntimeException("El stock no puede ser negativo");

        int stockAnterior = p.getStock();
        int diferencia = nuevoStock - stockAnterior;

        p.setStock(nuevoStock);
        productoRepo.save(p);

        MovimientoStock mov = new MovimientoStock();
        mov.setTipoCambio(TipoCambio.STOCK);
        mov.setProducto(p);
        mov.setValorNumerico(diferencia);
        mov.setValorAnterior((double) stockAnterior);
        mov.setValorActual((double) nuevoStock);
        mov.setMotivo(motivo);
        mov.setFecha(LocalDateTime.now());
        setUsuario(mov);

        return movimientoStockMapper.toDTO(movimientoStockRepository.save(mov));
    }

    // --- MÉTODOS PRIVADOS ---
    private Producto buscarProducto(Long id) {
        return productoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    private void setUsuario(MovimientoStock mov) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User user) {
            mov.setUser(user);
        }
    }

    private String normalizar(String input) {
        if (input == null || input.isEmpty()) return null;
        return Normalizer.normalize(input.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCOMBINING_DIACRITICAL_MARKS}+", "");
    }
}

