package com.nttdata.dockerized.postgresql.mapper;

import com.nttdata.dockerized.postgresql.model.dto.PedidoCreateRequestDto;
import com.nttdata.dockerized.postgresql.model.dto.PedidoResponseDto;
import com.nttdata.dockerized.postgresql.model.dto.PedidoUpdateRequestDto;
import com.nttdata.dockerized.postgresql.model.entity.DetallePedido;
import com.nttdata.dockerized.postgresql.model.entity.Pedido;
import com.nttdata.dockerized.postgresql.model.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", uses = {DetallePedidoMapper.class, UserMapper.class})
public interface PedidoMapper {

    // CREATE → de DTO a entidad
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaPedido", ignore = true)
    @Mapping(target = "detallesPedido", ignore = true)
    @Mapping(target = "user", ignore = true)
    Pedido toEntity(PedidoCreateRequestDto dto);

    // UPDATE → aplicamos solo los campos permitidos
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaPedido", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "detallesPedido", ignore = true) // se maneja aparte
    void updateEntityFromDto(PedidoUpdateRequestDto dto, @MappingTarget Pedido entity);

    // RESPONSE → de entidad a DTO
    @Mapping(target = "detalles", source = "detallesPedido")
    @Mapping(target = "total", expression = "java(pedido.getTotal())")
    PedidoResponseDto toResponseDto(Pedido pedido);

    List<PedidoResponseDto> toResponseList(List<Pedido> pedidos);
}
