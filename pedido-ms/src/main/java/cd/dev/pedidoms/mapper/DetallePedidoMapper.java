package cd.dev.pedidoms.mapper;


import cd.dev.pedidoms.model.dto.DetallePedidoCreateRequestDto;
import cd.dev.pedidoms.model.dto.DetallePedidoResponseDto;
import cd.dev.pedidoms.model.dto.DetallePedidoUpdateRequestDto;
import cd.dev.pedidoms.model.entity.DetallePedido;
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
    @Mapping(target = "productId", source = "producto.id")
    @Mapping(target = "subtotal", expression = "java(detallePedido.getSubtotal())")
    DetallePedidoResponseDto toResponseDto(DetallePedido detallePedido);

    List<DetallePedidoResponseDto> toResponseDtoList(List<DetallePedido> detallesPedido);
}