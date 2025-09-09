package com.nttdata.dockerized.postgresql.service.impl;

import com.nttdata.dockerized.postgresql.exception.NotFoundException;
import com.nttdata.dockerized.postgresql.mapper.DetallePedidoMapper;
import com.nttdata.dockerized.postgresql.mapper.PedidoMapper;
import com.nttdata.dockerized.postgresql.model.dto.DetallePedidoUpdateRequestDto;
import com.nttdata.dockerized.postgresql.model.dto.PedidoCreateRequestDto;
import com.nttdata.dockerized.postgresql.model.dto.PedidoResponseDto;
import com.nttdata.dockerized.postgresql.model.dto.PedidoUpdateRequestDto;
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


@Service
@RequiredArgsConstructor
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UserRepository userRepository;
    private final ProductRepository productoRepository;
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

        Pedido pedido = pedidoMapper.toEntity(request);
        pedido.setUser(user);

        List<DetallePedido> detalles = request.getDetalles().stream().map(detalleDto -> {
            Product producto = productoRepository.findById(detalleDto.getProductoId())
                    .orElseThrow(() -> new NotFoundException("Producto con id " + detalleDto.getProductoId() + " no encontrado"));

            DetallePedido detalle = detallePedidoMapper.toEntity(detalleDto);
            detalle.setPedido(pedido);
            detalle.setProducto(producto);

            return detalle;
        }).toList();

        pedido.setDetallesPedido(detalles);

        Pedido saved = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDto(saved);
    }

    @Override
    public PedidoResponseDto update(Long id, PedidoUpdateRequestDto request) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido con id " + id + " no encontrado"));

        pedidoMapper.updateEntityFromDto(request, pedido);

        if (request.getDetalles() != null && !request.getDetalles().isEmpty()) {
            for (DetallePedidoUpdateRequestDto detalleDto : request.getDetalles()) {
                DetallePedido detalleExistente = pedido.getDetallesPedido().stream()
                        .filter(d -> d.getId().equals(detalleDto.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NotFoundException(
                                "Detalle con id " + detalleDto.getId() + " no encontrado en este pedido"));

                detalleExistente.setCantidad(detalleDto.getCantidad());
                detalleExistente.setPrecioUnitario(detalleDto.getPrecioUnitario());
            }
        }

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