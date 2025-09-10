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
        // 1. Validar usuario
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("Usuario con id " + request.getUserId() + " no encontrado"));

        // 2. Crear pedido base
        Pedido pedido = new Pedido();
        pedido.setUser(user);
        pedido.setActive(true);

        // 3. Procesar y validar detalles
        List<DetallePedido> detalles = request.getDetalles().stream()
                .map(detalleDto -> {
                    // Validar que el producto existe y está activo
                    Product producto = productRepository.findById(detalleDto.getProductoId())
                            .orElseThrow(() -> new NotFoundException("Producto con id " + detalleDto.getProductoId() + " no encontrado"));

                    if (producto.getActive() != null && !producto.getActive()) {
                        throw new BadRequestException("El producto con id " + detalleDto.getProductoId() + " no está activo");
                    }

                    // Crear detalle
                    DetallePedido detalle = detallePedidoMapper.toEntity(detalleDto);
                    detalle.setPedido(pedido);
                    detalle.setProducto(producto);

                    return detalle;
                })
                .collect(Collectors.toList());

        // 4. Asignar detalles al pedido
        pedido.setDetallesPedido(detalles);

        // 5. Guardar (cascade se encarga de guardar los detalles)
        Pedido saved = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDto(saved);
    }

    @Override
    public PedidoResponseDto addDetalle(Long pedidoId, DetallePedidoCreateRequestDto detalleDto) {
        // 1. Buscar pedido
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido con id " + pedidoId + " no encontrado"));

        // 2. Validar que el pedido está activo
        if (pedido.getActive() != null && !pedido.getActive()) {
            throw new BadRequestException("No se pueden agregar detalles a un pedido inactivo");
        }

        // 3. Validar producto
        Product producto = productRepository.findById(detalleDto.getProductoId())
                .orElseThrow(() -> new NotFoundException("Producto con id " + detalleDto.getProductoId() + " no encontrado"));

        if (producto.getActive() != null && !producto.getActive()) {
            throw new BadRequestException("El producto con id " + detalleDto.getProductoId() + " no está activo");
        }

        // 4. Crear y agregar detalle
        DetallePedido detalle = detallePedidoMapper.toEntity(detalleDto);
        detalle.setPedido(pedido);
        detalle.setProducto(producto);

        pedido.getDetallesPedido().add(detalle);

        // 5. Guardar
        Pedido updated = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDto(updated);
    }

    @Override
    public PedidoResponseDto updateDetalle(Long pedidoId, Long detalleId, DetallePedidoUpdateRequestDto request) {
        // 1. Buscar pedido
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido con id " + pedidoId + " no encontrado"));

        // 2. Buscar detalle dentro del pedido
        DetallePedido detalle = pedido.getDetallesPedido().stream()
                .filter(d -> d.getId().equals(detalleId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("DetallePedido con id " + detalleId + " no encontrado en el pedido " + pedidoId));

        // 3. Validar que el pedido está activo
        if (pedido.getActive() != null && !pedido.getActive()) {
            throw new BadRequestException("No se pueden modificar detalles de un pedido inactivo");
        }

        // 4. Actualizar detalle
        detallePedidoMapper.updateEntityFromDto(request, detalle);

        // 5. Guardar
        Pedido updated = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDto(updated);
    }

    @Override
    public PedidoResponseDto removeDetalle(Long pedidoId, Long detalleId) {
        // 1. Buscar pedido
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido con id " + pedidoId + " no encontrado"));

        // 2. Validar que el pedido está activo
        if (pedido.getActive() != null && !pedido.getActive()) {
            throw new BadRequestException("No se pueden eliminar detalles de un pedido inactivo");
        }

        // 3. Buscar y eliminar detalle
        boolean removed = pedido.getDetallesPedido().removeIf(detalle -> detalle.getId().equals(detalleId));

        if (!removed) {
            throw new NotFoundException("DetallePedido con id " + detalleId + " no encontrado en el pedido " + pedidoId);
        }

        // 4. Validar que el pedido no quede vacío
        if (pedido.getDetallesPedido().isEmpty()) {
            throw new BadRequestException("Un pedido debe tener al menos un detalle");
        }

        // 5. Guardar
        Pedido updated = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDto(updated);
    }

    @Override
    public PedidoResponseDto update(Long id, PedidoUpdateRequestDto request) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido con id " + id + " no encontrado"));

        // Solo actualizar campos básicos del pedido, no los detalles
        pedidoMapper.updateEntityFromDto(request, pedido);
        Pedido updated = pedidoRepository.save(pedido);

        return pedidoMapper.toResponseDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido con id " + id + " no encontrado"));

        // El cascade se encargará de eliminar los detalles automáticamente
        pedidoRepository.delete(pedido);
    }
}