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
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DetallePedidoServiceImpl implements DetallePedidoService {
//
//    private final DetallePedidoRepository detallePedidoRepository;
//    private final PedidoRepository pedidoRepository;
//    private final ProductRepository productRepository;
//    private final DetallePedidoMapper detallePedidoMapper;
//
//    @Override
//    public DetallePedidoResponseDto save(Long pedidoId, DetallePedidoCreateRequestDto dto) {
//        Pedido pedido = pedidoRepository.findById(pedidoId)
//                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + pedidoId));
//
//        Product producto = productRepository.findById(dto.getProductoId())
//                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + dto.getProductoId()));
//
//        DetallePedido detallePedido = detallePedidoMapper.toEntity(dto);
//        detallePedido.setPedido(pedido);
//        detallePedido.setProducto(producto);
//
//        DetallePedido saved = detallePedidoRepository.save(detallePedido);
//        return detallePedidoMapper.toResponseDto(saved);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<DetallePedidoResponseDto> listByPedidoId(Long pedidoId) {
//        Pedido pedido = pedidoRepository.findById(pedidoId)
//                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + pedidoId));
//
//        List<DetallePedido> detalles = detallePedidoRepository.findByPedido(pedido);
//        return detallePedidoMapper.toResponseDtoList(detalles);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public DetallePedidoResponseDto findByIdAndPedidoId(Long detalleId, Long pedidoId) {
//        DetallePedido detalle = detallePedidoRepository.findByIdAndPedidoId(detalleId, pedidoId)
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "DetallePedido no encontrado con id: " + detalleId + " para pedidoId: " + pedidoId));
//        return detallePedidoMapper.toResponseDto(detalle);
//    }
//
//    @Override
//    public DetallePedidoResponseDto update(Long detalleId, Long pedidoId, DetallePedidoUpdateRequestDto dto) {
//        DetallePedido detalle = detallePedidoRepository.findByIdAndPedidoId(detalleId, pedidoId)
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "DetallePedido no encontrado con id: " + detalleId + " para pedidoId: " + pedidoId));
//
//        detallePedidoMapper.updateEntityFromDto(dto, detalle);
//
//        DetallePedido updated = detallePedidoRepository.save(detalle);
//        return detallePedidoMapper.toResponseDto(updated);
//    }
//
//    @Override
//    public void deleteByIdAndPedidoId(Long detalleId, Long pedidoId) {
//        DetallePedido detalle = detallePedidoRepository.findByIdAndPedidoId(detalleId, pedidoId)
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "DetallePedido no encontrado con id: " + detalleId + " para pedidoId: " + pedidoId));
//        detallePedidoRepository.delete(detalle);
//    }
}
