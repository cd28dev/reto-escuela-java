package com.nttdata.dockerized.postgresql.service;

import com.nttdata.dockerized.postgresql.model.dto.*;

import java.util.List;

public interface DetallePedidoService {
    List<DetallePedidoResponseDto> listAll();
    DetallePedidoResponseDto findById(Long id);
    DetallePedidoResponseDto save(DetallePedidoCreateRequestDto request);
    DetallePedidoResponseDto update(Long id, DetallePedidoUpdateRequestDto request);
    void deleteById(Long id);
}
