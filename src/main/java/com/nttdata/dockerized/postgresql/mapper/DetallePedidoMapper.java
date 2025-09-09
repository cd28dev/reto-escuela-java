package com.nttdata.dockerized.postgresql.mapper;

import com.nttdata.dockerized.postgresql.model.dto.DetallePedidoCreateRequestDto;
import com.nttdata.dockerized.postgresql.model.dto.DetallePedidoResponseDto;
import com.nttdata.dockerized.postgresql.model.dto.DetallePedidoUpdateRequestDto;
import com.nttdata.dockerized.postgresql.model.entity.DetallePedido;
import org.mapstruct.*;
import java.util.List;


@Mapper(componentModel = "spring")
public interface DetallePedidoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    @Mapping(target = "producto", ignore = true)
    DetallePedido toEntity(DetallePedidoCreateRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(DetallePedidoUpdateRequestDto dto, @MappingTarget DetallePedido detallePedido);

    @Mapping(target = "pedidoId", source = "pedido.id")
    @Mapping(target = "productoId", source = "producto.id")
    @Mapping(target = "subtotal", expression = "java(detallePedido.getSubtotal())")
    DetallePedidoResponseDto toResponseDto(DetallePedido detallePedido);

    List<DetallePedidoResponseDto> toResponseDtoList(List<DetallePedido> detallesPedido);
}