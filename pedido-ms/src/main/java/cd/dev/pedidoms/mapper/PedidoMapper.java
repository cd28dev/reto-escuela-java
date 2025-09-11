package cd.dev.pedidoms.mapper;

import cd.dev.pedidoms.model.dto.PedidoCreateRequestDto;
import cd.dev.pedidoms.model.dto.PedidoResponseDto;
import cd.dev.pedidoms.model.dto.PedidoUpdateRequestDto;
import cd.dev.pedidoms.model.entity.Pedido;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring")
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