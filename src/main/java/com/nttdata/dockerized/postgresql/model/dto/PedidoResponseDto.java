package com.nttdata.dockerized.postgresql.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PedidoSaveResponseDto {
    private Long id;
    private LocalDateTime fechaPedido;
    private String active;
    private UserDto user;
    private List<DetallePedidoDto> detallesPedido;
    private BigDecimal total;
}
