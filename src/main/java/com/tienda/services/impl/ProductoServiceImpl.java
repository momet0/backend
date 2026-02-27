package com.tienda.services.impl;

import com.tienda.dtos.producto.*;
import com.tienda.enums.*;
import com.tienda.mappers.*;
import com.tienda.models.*;
import com.tienda.repositories.*;
import com.tienda.repositories.specs.ProductoSpecs;
import com.tienda.services.IProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements IProductoService {

    private final ProductoRepository productoRepository;
    private final MovimientoStockRepository movimientoStockRepository;
    private final MovimientoStockServiceImpl movimientoStockService;
    private final ProductoDetalleMapper productoDetalleMapper;
    private final ProductoMapper productoMapper;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;
    private final MarcaRepository marcaRepository;
    private final CloudinaryService cloudinaryService;
    @Value("${CLOUDINARY_IMG_URL_DEFAULT}")
    private String imgUrlDefault;

    @Transactional(readOnly = true)
    public Page<ProductoDTO> obtenerProductos(int pagina, int tamaño, String ordenarPor, String direccion,
                                              ActividadProducto actividadProducto, String nombreBusqueda, Long categoria, Long proveedor,
                                              EstadoStock estadoStock, String marca){
        Sort.Direction sortDirection = "DESC".equalsIgnoreCase(direccion)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(sortDirection,ordenarPor);
        Pageable pageable = PageRequest.of(pagina,tamaño,sort);
        Specification<Producto> specs = nombreBusqueda != null ?
        ProductoSpecs.porFiltros(actividadProducto,sinAcentos(nombreBusqueda),categoria,proveedor,estadoStock, marca) : ProductoSpecs.porFiltros(actividadProducto,nombreBusqueda,categoria,proveedor,estadoStock, marca);
        Page<Producto> productos = productoRepository.findAll(specs,pageable);

        return productos.map(productoMapper::toDTO);
    }


    @Transactional
    public ProductoDetalleDTO guardarProducto(ProductoDetalleDTO productoDetalleDto, MultipartFile img){
        Producto prod = productoDetalleMapper.toEntity(productoDetalleDto);

        //Buscar marca
        if(productoDetalleDto.getMarca() != null){
            Marca marca = marcaRepository.findByNombre(productoDetalleDto.getMarca().getNombre())
                    .orElseThrow( () -> new RuntimeException("Marca no encontrada"));
            prod.setMarca(marca);
        }

        // buscar categoria real
        Categoria categoria = categoriaRepository.findById(productoDetalleDto.getCategoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // buscar proveedor real
        Proveedor proveedor = proveedorRepository.findById(productoDetalleDto.getProveedor().getId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        prod.setCategoria(categoria);
        prod.setProveedor(proveedor);

        prod.setActivo(true);
        if(img != null && !img.isEmpty()){
            prod.setImagenUrl(cloudinaryService.subirImagen(img));
        }else{
            prod.setImagenUrl(imgUrlDefault);
        }

        Producto guardado = productoRepository.save(prod);
        registrarMovimientoCreacion(guardado);
        return convertirProductoDTOCompleto(guardado);
    }

    @Transactional
    public ProductoDetalleDTO actualizarProducto(Long id, ProductoUpdateDTO productoUpdateDto, MultipartFile img) {

        Producto producto = productoRepository.findByIdWithCategoriaAndProveedor(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Boolean cambioPrecio = productoUpdateDto.getPrecio() != null &&
                !productoUpdateDto.getPrecio().equals(producto.getPrecio());

        Boolean cambioEstado = productoUpdateDto.getActivo() != null &&
                productoUpdateDto.getActivo() != producto.isActivo();
        boolean activando = cambioEstado && productoUpdateDto.getActivo();
        boolean cambioStock = productoUpdateDto.getStock() != null &&
                productoUpdateDto.getStock() != producto.getStock();

        // activar producto + cambiar stock = un solo movimiento
        if (activando && cambioStock) {
            int stockAnterior = producto.getStock();
            int nuevoStock = productoUpdateDto.getStock();

            producto.setActivo(true);
            producto.setStock(nuevoStock);

            registrarMovimientoEstado(producto, true, stockAnterior, nuevoStock);
        } else {
            // Casos normales (por separado)
            if (cambioEstado) {
                // Si está activando sin cambiar stock, el stock actual es el que tiene
                // Si está suspendiendo, el stock va a 0
                if (productoUpdateDto.getActivo()) {
                    // Activando sin cambiar stock
                    int stockActual = producto.getStock();
                    producto.setActivo(true);
                    registrarMovimientoEstado(producto, true, 0, stockActual);
                } else {
                    // Suspendiendo - esto se maneja en suspenderProducto()
                    // pero por si acaso lo llaman desde acá:
                    producto.setActivo(false);
                    registrarMovimientoEstado(producto, false, producto.getStock(), 0);
                    producto.setStock(0);
                }
            }
            if (cambioStock && !activando) {
                movimientoStockService.ajuste(id, productoUpdateDto.getStock(), MotivoMovimiento.AJUSTE_STOCK);
            }
        }

        if (cambioPrecio) {
            double precioAnterior = producto.getPrecio();
            double precioNuevo = productoUpdateDto.getPrecio();
            registrarCambioPrecio(producto, precioAnterior, precioNuevo);
        }

        if(img != null && !img.isEmpty()){
            producto.setImagenUrl(cloudinaryService.subirImagen(img));
        }
        actualizarCamposBasicos(producto, productoUpdateDto);

        return convertirProductoDTOCompleto(productoRepository.save(producto));
    }


    @Transactional
    public void suspenderProducto(Long id){
        Producto producto = productoRepository.findByIdWithCategoriaAndProveedor(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if(producto.isActivo()){
            int stockAnterior = producto.getStock();

            producto.setActivo(false);
            producto.setStock(0);

            // Movimiento de suspensión con salida de stock
            registrarMovimientoEstado(producto, false, stockAnterior, 0);

            productoRepository.save(producto);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDetalleDTO> findByCategoria(Long categoriaId) {
        List<Producto> productos = productoRepository.findByCategoriaId(categoriaId);
        return productos.stream().map(this::convertirProductoDTOCompleto).toList();
    }

    @Transactional(readOnly = true)
    public ProductoDetalleDTO obtenerProductoPorId(Long id){
        Producto producto = productoRepository.findByIdWithCategoriaAndProveedor(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return convertirProductoDTOCompleto(producto);
    }

    @Override
    public ProductoResumenDTO obtenerResumen() {
        ProductoResumenDTO dto = new ProductoResumenDTO();
        dto.setTotal(productoRepository.count());
        dto.setSinStock(productoRepository.countByStockAndActivoTrue(0));
        dto.setStockBajo(productoRepository.countProductosConStockBajo());
        dto.setActivos(productoRepository.countByActivoTrue());
        dto.setInactivos(productoRepository.countByActivoFalse());
        return dto;
    }

    @Override
    public ProductoDetalleDTO obtenerPorCodigodeBarras(String codigoBarra) {
        Producto producto = productoRepository.findByCodigoBarra(codigoBarra).orElseThrow( () -> new RuntimeException("Producto no encontrado"));
        return convertirProductoDTOCompleto(producto);
    }

    private void registrarMovimientoEstado(Producto producto, boolean activo, int stockAnterior, int stockActual){
        MovimientoStock mov = new MovimientoStock();
        mov.setTipoCambio(TipoCambio.STOCK);
        mov.setProducto(producto);
        mov.setValorNumerico(stockActual - stockAnterior);
        mov.setValorAnterior((double) stockAnterior);
        mov.setValorActual((double) stockActual);
        mov.setFecha(LocalDateTime.now());
        mov.setMotivo(activo ? MotivoMovimiento.ACTIVACION_PRODUCTO : MotivoMovimiento.SUSPENSION_PRODUCTO);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.getPrincipal() instanceof User user){
            mov.setUser(user);
        }
        movimientoStockRepository.save(mov);
    }

    private void actualizarCamposBasicos(Producto p, ProductoUpdateDTO dto){
        if (dto.getNombre() != null) p.setNombre(dto.getNombre());
        if (dto.getPrecio() != null) p.setPrecio(dto.getPrecio());
        if (dto.getMarca() != null){
            Marca marca = marcaRepository.findByNombre(dto.getMarca().getNombre())
                    .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
            p.setMarca(marca);
        }
    }

    private ProductoDetalleDTO convertirProductoDTOCompleto(Producto producto) {
        return productoDetalleMapper.toDTO(producto);
    }

    private String sinAcentos(String nombreBusqueda){
        return Normalizer.normalize(nombreBusqueda, Normalizer.Form.NFD)
                .replaceAll("\\p{InCOMBINING_DIACRITICAL_MARKS}+","");
    }

    @Override
    @Transactional
    public void modificarPrecios(ModificarPrecioRequest request) {
        // Validar que solo venga proveedor o marca
        if (request.getProveedorId() != null && request.getMarcaId() != null) {
            throw new RuntimeException("Seleccionar solo proveedor o marca");
        }
        if (request.getProveedorId() == null && request.getMarcaId() == null) {
            throw new RuntimeException("Debe especificar proveedor o marca");
        }

        // Valida que tipoOperacion no sea null
        if (request.getTipoOperacion() == null) {
            throw new RuntimeException("El tipo de operación es obligatorio");
        }

        // Buscar productos
        List<Producto> productos;
        Marca marca = null;
        Proveedor proveedor = null;

        if (request.getProveedorId() != null) {
            productos = productoRepository.findByProveedorIdAndActivoTrue(request.getProveedorId());
            proveedor = proveedorRepository.findById(request.getProveedorId())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        } else {
            productos = productoRepository.findByMarcaIdAndActivoTrue(request.getMarcaId());
            marca = marcaRepository.findById(request.getMarcaId())
                    .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
        }

        // Validar que haya productos
        if (productos.isEmpty()) {
            throw new RuntimeException("No hay productos activos para el filtro seleccionado");
        }

        double precioPromedioAnterior = productos.stream()
                .mapToDouble(Producto::getPrecio)
                .average()
                .orElse(0.0);

        // Calcular el factor según el tipo de operación
        double factor = switch (request.getTipoOperacion()) {
            case AUMENTO -> 1 + (request.getPorcentaje() / 100.0);
            case DESCUENTO -> 1 - (request.getPorcentaje() / 100.0);
        };

        for (Producto producto : productos) {
            double nuevoPrecio = producto.getPrecio() * factor;
            long precioRedondeado = redondeoEscalonado(nuevoPrecio);

            // Evitar precios negativos o en cero
            if (precioRedondeado <= 0) {
                throw new RuntimeException("El descuento aplicado dejaría el producto '" +
                        producto.getNombre() + "' con precio inválido: " + precioRedondeado);
            }

            producto.setPrecio((double)precioRedondeado);
            productoRepository.save(producto);
        }

        // === CALCULAR PRECIO PROMEDIO DESPUÉS ===
        double precioPromedioNuevo = productos.stream()
                .mapToDouble(Producto::getPrecio)
                .average()
                .orElse(0.0);

        // Redondeamos los promedios para que el historial no tenga decimales feos
        double promedioAnteriorProlijo = (double) redondeoEscalonado(precioPromedioAnterior);
        double promedioNuevoProlijo = (double) redondeoEscalonado(precioPromedioNuevo);

        // === REGISTRAR UN SOLO MOVIMIENTO MASIVO ===
        registrarCambioPrecioMasivo(
                marca,
                proveedor,
                request.getPorcentaje(),
                request.getTipoOperacion(),
                promedioAnteriorProlijo,
                promedioNuevoProlijo
        );
    }

    private long redondeoEscalonado(double precio) {
        long base = (long) (precio / 100);
        int resto = (int) (precio % 100);
        if (resto == 0) {
            return base * 100;
        } else if (resto < 50) {
            return base * 100 + 50;
        } else {
            return (base + 1) * 100;
        }
    }

    private void registrarMovimientoCreacion(Producto producto) {
        MovimientoStock mov = new MovimientoStock();
        mov.setTipoCambio(TipoCambio.STOCK);
        mov.setProducto(producto);
        mov.setValorNumerico(producto.getStock());
        mov.setValorAnterior(0.0); // Antes de crear no había stock
        mov.setValorActual((double) producto.getStock()); // Stock inicial
        mov.setFecha(LocalDateTime.now());
        mov.setMotivo(MotivoMovimiento.CREACION_PRODUCTO);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User user) {
            mov.setUser(user);
        }
        movimientoStockRepository.save(mov);
    }

    private void registrarCambioPrecio(Producto producto, double precioAnterior, double precioNuevo) {
        int porcentaje = calcularPorcentaje(precioAnterior, precioNuevo);

        MovimientoStock mov = new MovimientoStock();
        mov.setTipoCambio(TipoCambio.PRECIO);
        mov.setProducto(producto);
        mov.setMarca(null);
        mov.setProveedor(null);
        mov.setValorNumerico(porcentaje);
        mov.setValorAnterior(precioAnterior);
        mov.setValorActual(precioNuevo);
        mov.setFecha(LocalDateTime.now());
        mov.setMotivo(MotivoMovimiento.AJUSTE_PRECIO_MANUAL);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User user) {
            mov.setUser(user);
        }
        movimientoStockRepository.save(mov);
    }

    private int calcularPorcentaje(double anterior, double nuevo) {
        if (anterior == 0) return 0;
        return (int) Math.round(((nuevo - anterior) / anterior) * 100);
    }

    private void registrarCambioPrecioMasivo(
            Marca marca,
            Proveedor proveedor,
            int porcentaje,
            TipoOperacionPrecio tipoOperacion,
            double precioPromedioAnterior,
            double precioPromedioNuevo) {

        MovimientoStock mov = new MovimientoStock();
        mov.setTipoCambio(TipoCambio.PRECIO);

        // Sin producto específico (es masivo)
        mov.setProducto(null);

        // Solo uno de estos dos va a tener valor
        mov.setMarca(marca);
        mov.setProveedor(proveedor);

        // Porcentaje: positivo o negativo
        mov.setValorNumerico(tipoOperacion == TipoOperacionPrecio.AUMENTO ? porcentaje : -porcentaje);

        // Precios promedio como referencia
        mov.setValorAnterior(0.0);
        mov.setValorActual(0.0);

        mov.setFecha(LocalDateTime.now());
        mov.setMotivo(tipoOperacion == TipoOperacionPrecio.AUMENTO
                ? MotivoMovimiento.AUMENTO_MASIVO_PORCENTAJE
                : MotivoMovimiento.DISMINUCION_MASIVA_PORCENTAJE);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User user) {
            mov.setUser(user);
        }
        movimientoStockRepository.save(mov);
    }
}
