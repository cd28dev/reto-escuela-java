package com.nttdata.dockerized.postgresql.service;

import com.nttdata.dockerized.postgresql.model.dto.*;

import java.util.List;

public interface PedidoService {
    List<PedidoResponseDto> listAll();
    PedidoResponseDto findById(Long id);
    PedidoResponseDto save(PedidoCreateRequestDto request);
    PedidoResponseDto update(Long id, PedidoUpdateRequestDto request);
    void deleteById(Long id);
}
