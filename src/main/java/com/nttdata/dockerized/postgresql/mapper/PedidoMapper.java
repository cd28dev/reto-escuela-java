package com.nttdata.dockerized.postgresql.mapper;

import com.nttdata.dockerized.postgresql.model.dto.PedidoSaveRequestDto;
import com.nttdata.dockerized.postgresql.model.dto.PedidoSaveResponseDto;
import com.nttdata.dockerized.postgresql.model.entity.DetallePedido;
import com.nttdata.dockerized.postgresql.model.entity.Pedido;
import com.nttdata.dockerized.postgresql.model.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

@Mapper(uses = {BooleanMapper.class, UserMapper.class, DetallePedidoMapper.class})
public interface PedidoMapper {
    BooleanMapper booleanMapper = Mappers.getMapper(BooleanMapper.class);
    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);

    public PedidoDto map(Pedido pedido);

    public List<PedidoDto> map(List<Pedido> pedidos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaPedido", ignore = true)
    @Mapping(target = "user", source = "userId", qualifiedByName = "userIdToUser")
    @Mapping(target = "detallesPedido", source = "detalles")
    public Pedido toEntity(PedidoSaveRequestDto pedidoSaveRequestDto);


    public PedidoSaveResponseDto toPedidoSaveResponseDto(Pedido pedido);

    default Pedido toEntityForUpdate(Long id, PedidoSaveRequestDto dto, Pedido existingPedido) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO de actualización no puede ser null");
        }
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser null");
        }

        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setFechaPedido(existingPedido.getFechaPedido());

        if (dto.getActive() != null) {
            pedido.setActive(booleanMapper.stringToBoolean(dto.getActive()));
        } else {
            pedido.setActive(existingPedido.getActive());
        }

        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            pedido.setUser(user);
        } else {
            pedido.setUser(existingPedido.getUser());
        }

        if (dto.getDetalles() != null && !dto.getDetalles().isEmpty()) {
            List<DetallePedido> detalles = DetallePedidoMapper.INSTANCE.toEntityList(dto.getDetalles());
            detalles.forEach(detalle -> detalle.setPedido(pedido));
            pedido.setDetallesPedido(detalles);
        } else {
            pedido.setDetallesPedido(existingPedido.getDetallesPedido());
        }

        return pedido;
    }

    @Named("userIdToUser")
    default User userIdToUser(Long userId) {
        if (userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }

    default BigDecimal calcularTotal(Pedido pedido) {
        if (pedido.getDetallesPedido() == null || pedido.getDetallesPedido().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return pedido.getDetallesPedido().stream()
                .map(detalle -> detalle.getPrecioUnitario()
                        .multiply(new BigDecimal(detalle.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @AfterMapping
    default void setRelacionesBidireccionales(@MappingTarget Pedido pedido) {
        if (pedido.getDetallesPedido() != null) {
            pedido.getDetallesPedido().forEach(detalle -> detalle.setPedido(pedido));
        }
    }

    @AfterMapping
    default void setRemainingValues(Pedido pedido, @MappingTarget PedidoDto pedidoDto) {
        pedidoDto.setActive(Boolean.TRUE.equals(pedido.getActive()) ? "Active" : "Inactive");
    }

    @AfterMapping
    default void setRemainingValuesResponse(Pedido pedido, @MappingTarget PedidoSaveResponseDto responseDto) {
        responseDto.setActive(Boolean.TRUE.equals(pedido.getActive()) ? "Active" : "Inactive");
    }
}
