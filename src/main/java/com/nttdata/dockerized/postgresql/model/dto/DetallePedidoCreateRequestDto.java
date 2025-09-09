package com.nttdata.dockerized.postgresql.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DetallePedidoSaveRequestDto {
    private Long productoId;

    private Integer cantidad;

    private BigDecimal precioUnitario;
}
