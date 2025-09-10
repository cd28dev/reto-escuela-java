package com.nttdata.dockerized.postgresql.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PedidoResponseDto {

    private Long id;
    private LocalDateTime fechaPedido;
    private Boolean active;
    private UserResponseDto user;
    private List<DetallePedidoResponseDto> detalles;
    private BigDecimal total;

}
