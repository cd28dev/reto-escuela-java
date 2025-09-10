package com.nttdata.dockerized.postgresql.service.impl;

import com.nttdata.dockerized.postgresql.exception.BadRequestException;
import com.nttdata.dockerized.postgresql.exception.NotFoundException;
import com.nttdata.dockerized.postgresql.mapper.DetallePedidoMapper;
import com.nttdata.dockerized.postgresql.mapper.PedidoMapper;
import com.nttdata.dockerized.postgresql.model.dto.*;
import com.nttdata.dockerized.postgresql.model.entity.DetallePedido;
import com.nttdata.dockerized.postgresql.model.entity.Pedido;
import com.nttdata.dockerized.postgresql.model.entity.Product;
import com.nttdata.dockerized.postgresql.model.entity.User;
import com.nttdata.dockerized.postgresql.repository.PedidoRepository;
import com.nttdata.dockerized.postgresql.repository.ProductRepository;
import com.nttdata.dockerized.postgresql.repository.UserRepository;
import com.nttdata.dockerized.postgresql.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PedidoMapper pedidoMapper;
    private final DetallePedidoMapper detallePedidoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDto> listAll() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidoMapper.toResponseList(pedidos);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponseDto findById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido con id " + id + " no encontrado"));
        return pedidoMapper.toResponseDto(pedido);
    }

    @Override
    public PedidoResponseDto save(PedidoCreateRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("Usuario con id " + request.getUserId() + " no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setUser(user);
        pedido.setActive(true);

        List<DetallePedido> detalles = request.getDetalles().stream()
                .map(detalleDto -> {
                    Product producto = productRepository.findById(detalleDto.getProductoId())
                            .orElseThrow(() -> new NotFoundException("Producto con id " + detalleDto.getProductoId() + " no encontrado"));

                    if (producto.getActive() != null && !producto.getActive()) {
                        throw new BadRequestException("El producto con id " + detalleDto.getProductoId() + " no está activo");
                    }

                    DetallePedido detalle = detallePedidoMapper.toEntity(detalleDto);
                    detalle.setPedido(pedido);
                    detalle.setProducto(producto);

                    return detalle;
                })
                .collect(Collectors.toList());

        pedido.setDetallesPedido(detalles);

        Pedido saved = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDto(saved);
    }

    @Override
    public PedidoResponseDto addDetalle(Long pedidoId, DetallePedidoCreateRequestDto detalleDto) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido con id " + pedidoId + " no encontrado"));

        if (pedido.getActive() != null && !pedido.getActive()) {
            throw new BadRequestException("No se pueden agregar detalles a un pedido inactivo");
        }

        Product producto = productRepository.findById(detalleDto.getProductoId())
                .orElseThrow(() -> new NotFoundException("Producto con id " + detalleDto.getProductoId() + " no encontrado"));

        if (producto.getActive() != null && !producto.getActive()) {
            throw new BadRequestException("El producto con id " + detalleDto.getProductoId() + " no está activo");
        }

        DetallePedido detalle = detallePedidoMapper.toEntity(detalleDto);
        detalle.setPedido(pedido);
        detalle.setProducto(producto);

        pedido.getDetallesPedido().add(detalle);

        Pedido updated = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDto(updated);
    }

    @Override
    public PedidoResponseDto updateDetalle(Long pedidoId, Long detalleId, DetallePedidoUpdateRequestDto request) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido con id " + pedidoId + " no encontrado"));

        DetallePedido detalle = pedido.getDetallesPedido().stream()
                .filter(d -> d.getId().equals(detalleId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("DetallePedido con id " + detalleId + " no encontrado en el pedido " + pedidoId));

        if (pedido.getActive() != null && !pedido.getActive()) {
            throw new BadRequestException("No se pueden modificar detalles de un pedido inactivo");
        }

        detallePedidoMapper.updateEntityFromDto(request, detalle);

        Pedido updated = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDto(updated);
    }

    @Override
    public PedidoResponseDto removeDetalle(Long pedidoId, Long detalleId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido con id " + pedidoId + " no encontrado"));

        if (pedido.getActive() != null && !pedido.getActive()) {
            throw new BadRequestException("No se pueden eliminar detalles de un pedido inactivo");
        }

        boolean removed = pedido.getDetallesPedido().removeIf(detalle -> detalle.getId().equals(detalleId));

        if (!removed) {
            throw new NotFoundException("DetallePedido con id " + detalleId + " no encontrado en el pedido " + pedidoId);
        }

        if (pedido.getDetallesPedido().isEmpty()) {
            throw new BadRequestException("Un pedido debe tener al menos un detalle");
        }

        Pedido updated = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDto(updated);
    }

    @Override
    public PedidoResponseDto update(Long id, PedidoUpdateRequestDto request) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido con id " + id + " no encontrado"));

        pedidoMapper.updateEntityFromDto(request, pedido);
        Pedido updated = pedidoRepository.save(pedido);

        return pedidoMapper.toResponseDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido con id " + id + " no encontrado"));

        pedidoRepository.delete(pedido);
    }
}