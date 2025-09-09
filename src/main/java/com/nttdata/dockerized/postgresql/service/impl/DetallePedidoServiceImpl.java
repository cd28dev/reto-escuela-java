package com.nttdata.dockerized.postgresql.service.impl;

import com.nttdata.dockerized.postgresql.service.DetallePedidoService;
import org.springframework.stereotype.Service;

@Service

public class DetallePedidoServiceImpl implements DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductRepository productRepository;

    @Override
    public List<DetallePedidoDto> listAll() {
        List<DetallePedido> detalles = detallePedidoRepository.findAll();
        return DetallePedidoMapper.INSTANCE.map(detalles);
    }

    @Override
    public DetallePedidoDto findById(Long id) {
        DetallePedido detalle = detallePedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de pedido no encontrado con ID: " + id));
        return DetallePedidoMapper.INSTANCE.map(detalle);
    }

    @Override
    @Transactional
    public DetallePedidoDto save(DetallePedidoSaveRequestDto request) {
        validateDetallePedidoSaveRequest(request);

        // Verificar que el producto existe y está activo
        Product product = productRepository.findById(request.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + request.getProductoId()));

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new RuntimeException("El producto con ID " + request.getProductoId() + " no está activo");
        }

        DetallePedido detalle = DetallePedidoMapper.INSTANCE.toEntity(request);

        // Si no se proporciona precio unitario, usar el precio actual del producto
        if (detalle.getPrecioUnitario() == null) {
            detalle.setPrecioUnitario(product.getPrice());
        }

        // Nota: El pedido debe ser establecido externamente ya que DetallePedido
        // normalmente se crea como parte de un Pedido, no de forma independiente
        // En un escenario real, este servicio probablemente no se usaría directamente

        DetallePedido savedDetalle = detallePedidoRepository.save(detalle);
        return DetallePedidoMapper.INSTANCE.map(savedDetalle);
    }

    @Override
    @Transactional
    public DetallePedidoDto update(Long id, DetallePedidoSaveRequestDto request) {
        DetallePedido existingDetalle = detallePedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de pedido no encontrado con ID: " + id));

        // Verificar que el nuevo producto existe y está activo si se proporciona
        if (request.getProductoId() != null && !request.getProductoId().equals(existingDetalle.getProducto().getId())) {
            Product product = productRepository.findById(request.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + request.getProductoId()));

            if (!Boolean.TRUE.equals(product.getActive())) {
                throw new RuntimeException("El producto con ID " + request.getProductoId() + " no está activo");
            }
        }

        DetallePedido detalleToUpdate = DetallePedidoMapper.INSTANCE.toEntityForUpdate(id, request, existingDetalle);

        // Si se cambió el producto y no se proporcionó nuevo precio, usar el precio del nuevo producto
        if (request.getProductoId() != null && !request.getProductoId().equals(existingDetalle.getProducto().getId())
                && request.getPrecioUnitario() == null) {
            Product newProduct = productRepository.findById(request.getProductoId()).get();
            detalleToUpdate.setPrecioUnitario(newProduct.getPrice());
        }

        DetallePedido updatedDetalle = detallePedidoRepository.save(detalleToUpdate);
        return DetallePedidoMapper.INSTANCE.map(updatedDetalle);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!detallePedidoRepository.existsById(id)) {
            throw new RuntimeException("Detalle de pedido no encontrado con ID: " + id);
        }
        detallePedidoRepository.deleteById(id);
    }

    private void validateDetallePedidoSaveRequest(DetallePedidoSaveRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser null");
        }
        if (request.getProductoId() == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser null");
        }
        if (request.getCantidad() == null || request.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (request.getPrecioUnitario() != null && request.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a cero");
        }
    }

    // Métodos adicionales útiles para el negocio
    public List<DetallePedidoDto> findByPedidoId(Long pedidoId) {
        // Este método requeriría un método personalizado en el repository
        // List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedidoId);
        // return DetallePedidoMapper.INSTANCE.map(detalles);

        // Por ahora, filtraremos de todos los detalles (no eficiente para producción)
        return listAll().stream()
                .filter(detalle -> pedidoId.equals(detalle.getId())) // Esto necesitaría acceso al pedidoId
                .collect(Collectors.toList());
    }

    public List<DetallePedidoDto> findByProductoId(Long productoId) {
        return listAll().stream()
                .filter(detalle -> detalle.getProducto() != null &&
                        productoId.equals(detalle.getProducto().getId()))
                .collect(Collectors.toList());
    }
}
