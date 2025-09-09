package com.nttdata.dockerized.postgresql.service.impl;

import com.nttdata.dockerized.postgresql.exception.ResourceNotFoundException;
import com.nttdata.dockerized.postgresql.mapper.DetallePedidoMapper;
import com.nttdata.dockerized.postgresql.model.dto.DetallePedidoCreateRequestDto;
import com.nttdata.dockerized.postgresql.model.dto.DetallePedidoResponseDto;
import com.nttdata.dockerized.postgresql.model.dto.DetallePedidoUpdateRequestDto;
import com.nttdata.dockerized.postgresql.model.entity.DetallePedido;
import com.nttdata.dockerized.postgresql.model.entity.Pedido;
import com.nttdata.dockerized.postgresql.model.entity.Product;
import com.nttdata.dockerized.postgresql.repository.DetallePedidoRepository;
import com.nttdata.dockerized.postgresql.repository.PedidoRepository;
import com.nttdata.dockerized.postgresql.repository.ProductRepository;
import com.nttdata.dockerized.postgresql.service.DetallePedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DetallePedidoServiceImpl implements DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductRepository productRepository;
    private final DetallePedidoMapper detallePedidoMapper;

    @Override
    public List<DetallePedidoResponseDto> listAll() {
        List<DetallePedido> detalles = detallePedidoRepository.findAll();
        return detallePedidoMapper.toResponseDtoList(detalles);
    }

    @Override
    public DetallePedidoResponseDto findById(Long id) {
        DetallePedido detallePedido = detallePedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetallePedido no encontrado con id: " + id));
        return detallePedidoMapper.toResponseDto(detallePedido);
    }

    @Override
    public DetallePedidoResponseDto save(DetallePedidoCreateRequestDto dto) {
        Pedido pedido = pedidoRepository.findById(dto.getPedidoId())
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + dto.getPedidoId()));

        Product producto = productRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + dto.getProductoId()));

        DetallePedido detallePedido = detallePedidoMapper.toEntity(dto);
        detallePedido.setPedido(pedido);
        detallePedido.setProducto(producto);

        DetallePedido saved = detallePedidoRepository.save(detallePedido);
        return detallePedidoMapper.toResponseDto(saved);
    }

    @Override
    public DetallePedidoResponseDto update(Long id, DetallePedidoUpdateRequestDto dto) {
        DetallePedido detallePedido = detallePedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetallePedido no encontrado con id: " + id));

        detallePedidoMapper.updateEntityFromDto(dto, detallePedido);

        DetallePedido updated = detallePedidoRepository.save(detallePedido);
        return detallePedidoMapper.toResponseDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        if (!detallePedidoRepository.existsById(id)) {
            throw new ResourceNotFoundException("DetallePedido no encontrado con id: " + id);
        }
        detallePedidoRepository.deleteById(id);
    }
}
