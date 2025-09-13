package cd.dev.pedidoms.mapper;

import cd.dev.pedidoms.model.dto.PedidoCreateRequestDto;
import cd.dev.pedidoms.model.dto.PedidoResponseDto;
import cd.dev.pedidoms.model.dto.PedidoUpdateRequestDto;
import cd.dev.pedidoms.model.entity.Pedido;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring")
public interface PedidoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaPedido", ignore = true)
    @Mapping(target = "detallesPedido", ignore = true)
    Pedido toEntity(PedidoCreateRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaPedido", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "detallesPedido", ignore = true)
    void updateEntityFromDto(PedidoUpdateRequestDto dto, @MappingTarget Pedido entity);

    @Mapping(target = "detalles", source = "detallesPedido")
    @Mapping(target = "total", expression = "java(pedido.getTotal())")
    PedidoResponseDto toResponseDto(Pedido pedido);

    List<PedidoResponseDto> toResponseList(List<Pedido> pedidos);
}