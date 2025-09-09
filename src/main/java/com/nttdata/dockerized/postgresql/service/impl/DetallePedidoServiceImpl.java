package com.nttdata.dockerized.postgresql.service.impl;

import com.nttdata.dockerized.postgresql.mapper.DetallePedidoMapper;
import com.nttdata.dockerized.postgresql.model.dto.DetallePedidoSaveRequestDto;
import com.nttdata.dockerized.postgresql.model.dto.DetallePedidoSaveResponseDto;
import com.nttdata.dockerized.postgresql.model.entity.DetallePedido;
import com.nttdata.dockerized.postgresql.model.entity.Product;
import com.nttdata.dockerized.postgresql.repository.DetallePedidoRepository;
import com.nttdata.dockerized.postgresql.repository.PedidoRepository;
import com.nttdata.dockerized.postgresql.repository.ProductRepository;
import com.nttdata.dockerized.postgresql.service.DetallePedidoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class DetallePedidoServiceImpl implements DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductRepository productRepository;

    @Override
    public List<DetallePedidoDto> listAll() {
        List<DetallePedido> detalles = (List<DetallePedido>) detallePedidoRepository.findAll();
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
    public DetallePedidoSaveResponseDto save(DetallePedidoSaveRequestDto request) {

        Product product = productRepository.findById(request.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + request.getProductoId()));

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new RuntimeException("El producto con ID " + request.getProductoId() + " no está activo");
        }

        DetallePedido detalle = DetallePedidoMapper.INSTANCE.toEntity(request);

        if (detalle.getPrecioUnitario() == null) {
            detalle.setPrecioUnitario(product.getPrice());
        }

        DetallePedidoSaveResponseDto savedDetalle = detallePedidoRepository.save(detalle);
        return DetallePedidoMapper.INSTANCE.map(savedDetalle);
    }

    @Override
    @Transactional
    public DetallePedidoDto update(Long id, DetallePedidoSaveRequestDto request) {
        DetallePedido existingDetalle = detallePedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de pedido no encontrado con ID: " + id));

        if (request.getProductoId() != null && !request.getProductoId().equals(existingDetalle.getProducto().getId())) {
            Product product = productRepository.findById(request.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + request.getProductoId()));

            if (!Boolean.TRUE.equals(product.getActive())) {
                throw new RuntimeException("El producto con ID " + request.getProductoId() + " no está activo");
            }
        }

        DetallePedido detalleToUpdate = DetallePedidoMapper.INSTANCE.toEntityForUpdate(id, request, existingDetalle);

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

}
