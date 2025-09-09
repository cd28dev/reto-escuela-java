package com.nttdata.dockerized.postgresql.service;

import com.nttdata.dockerized.postgresql.model.dto.*;
import com.nttdata.dockerized.postgresql.model.entity.DetallePedido;

import java.util.List;

public interface DetallePedidoService {
    List<DetallePedidoDto> listAll();
    DetallePedidoDto findById(Long id);
    DetallePedidoSaveResponseDto save(DetallePedidoSaveRequestDto request);
    DetallePedidoDto update(Long id, DetallePedidoSaveRequestDto request);
    void deleteById(Long id);
}
