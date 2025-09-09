package com.nttdata.dockerized.postgresql.mapper;

import com.nttdata.dockerized.postgresql.model.dto.DetallePedidoSaveRequestDto;
import com.nttdata.dockerized.postgresql.model.entity.DetallePedido;
import com.nttdata.dockerized.postgresql.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

@Mapper(uses = {ProductMapper.class})
public interface DetallePedidoMapper {


    DetallePedidoMapper INSTANCE = Mappers.getMapper(DetallePedidoMapper.class);

    public DetallePedidoDto map(DetallePedido detallePedido);

    public List<DetallePedidoDto> map(List<DetallePedido> detallesPedido);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    @Mapping(target = "producto", source = "productoId", qualifiedByName = "productoIdToProduct")
    public DetallePedido toEntity(DetallePedidoSaveRequestDto detallePedidoSaveRequestDto);

    public List<DetallePedido> toEntityList(List<DetallePedidoSaveRequestDto> requestDTOList);

    default DetallePedido toEntityForUpdate(Long id, DetallePedidoSaveRequestDto dto, DetallePedido existingDetalle) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO de actualización no puede ser null");
        }
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser null");
        }

        DetallePedido detalle = new DetallePedido();
        detalle.setId(id);
        detalle.setPedido(existingDetalle.getPedido());

        if (dto.getProductoId() != null) {
            Product product = new Product();
            product.setId(dto.getProductoId());
            detalle.setProducto(product);
        } else {
            detalle.setProducto(existingDetalle.getProducto());
        }

        if (dto.getCantidad() != null && dto.getCantidad() > 0) {
            detalle.setCantidad(dto.getCantidad());
        } else {
            detalle.setCantidad(existingDetalle.getCantidad());
        }

        if (dto.getPrecioUnitario() != null && dto.getPrecioUnitario().compareTo(BigDecimal.ZERO) > 0) {
            detalle.setPrecioUnitario(dto.getPrecioUnitario());
        } else {
            detalle.setPrecioUnitario(existingDetalle.getPrecioUnitario());
        }

        return detalle;
    }

    @Named("productoIdToProduct")
    default Product productoIdToProduct(Long productoId) {
        if (productoId == null) return null;
        Product product = new Product();
        product.setId(productoId);
        return product;
    }

    default BigDecimal calcularSubtotal(DetallePedido detallePedido) {
        if (detallePedido.getCantidad() == null || detallePedido.getPrecioUnitario() == null) {
            return BigDecimal.ZERO;
        }
        return detallePedido.getPrecioUnitario()
                .multiply(new BigDecimal(detallePedido.getCantidad()));
    }
}
