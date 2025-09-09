package com.nttdata.dockerized.postgresql.service.impl;

import com.nttdata.dockerized.postgresql.mapper.PedidoMapper;
import com.nttdata.dockerized.postgresql.model.dto.PedidoSaveRequestDto;
import com.nttdata.dockerized.postgresql.model.dto.PedidoSaveResponseDto;
import com.nttdata.dockerized.postgresql.model.entity.Pedido;
import com.nttdata.dockerized.postgresql.model.entity.Product;
import com.nttdata.dockerized.postgresql.repository.PedidoRepository;
import com.nttdata.dockerized.postgresql.repository.ProductRepository;
import com.nttdata.dockerized.postgresql.repository.UserRepository;
import com.nttdata.dockerized.postgresql.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<PedidoDto> listAll() {
        List<Pedido> pedidos = (List<Pedido>) pedidoRepository.findAll();
        return PedidoMapper.INSTANCE.map(pedidos);
    }

    @Override
    public PedidoDto findById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
        return PedidoMapper.INSTANCE.map(pedido);
    }

    @Override
    @Transactional
    public PedidoSaveResponseDto save(PedidoSaveRequestDto request) {
        validatePedidoSaveRequest(request);

        userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + request.getUserId()));


        Pedido pedido = PedidoMapper.INSTANCE.toEntity(request);

        if (pedido.getActive() == null) {
            pedido.setActive(true);
        }

        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(LocalDateTime.now());
        }

        if (pedido.getDetallesPedido() != null) {
            pedido.getDetallesPedido().forEach(detalle -> {
                detalle.setPedido(pedido);
                Product product = productRepository.findById(detalle.getProducto().getId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                detalle.setPrecioUnitario(product.getPrice());
            });
        }

        Pedido savedPedido = pedidoRepository.save(pedido);

        PedidoSaveResponseDto response = PedidoMapper.INSTANCE.toPedidoSaveResponseDto(savedPedido);
        // Calcular y establecer el total
        response.setTotal(PedidoMapper.INSTANCE.calcularTotal(savedPedido));

        return response;
    }

    @Override
    @Transactional
    public PedidoDto update(Long id, PedidoSaveRequestDto request) {
        Pedido existingPedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));

        if (request.getUserId() != null && !request.getUserId().equals(existingPedido.getUser().getId())) {
            userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + request.getUserId()));
        }

        Pedido pedidoToUpdate = PedidoMapper.INSTANCE.toEntityForUpdate(id, request, existingPedido);

        if (request.getDetalles() != null && !request.getDetalles().isEmpty()) {
            pedidoToUpdate.getDetallesPedido().forEach(detalle -> {
                Product product = productRepository.findById(detalle.getProducto().getId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                detalle.setPrecioUnitario(product.getPrice());
            });
        }

        Pedido updatedPedido = pedidoRepository.save(pedidoToUpdate);
        return PedidoMapper.INSTANCE.map(updatedPedido);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("Pedido no encontrado con ID: " + id);
        }
        pedidoRepository.deleteById(id);
    }

    private void validatePedidoSaveRequest(PedidoSaveRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser null");
        }
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("El ID de usuario no puede ser null");
        }
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un detalle");
        }
    }

}
