package com.nttdata.dockerized.postgresql.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PedidoCreateRequestDto {
    @NotNull
    private Long userId;

    @NotEmpty
    private List<DetallePedidoCreateRequestDto> detalles;
}
