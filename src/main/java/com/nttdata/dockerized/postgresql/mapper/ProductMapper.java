package com.nttdata.dockerized.postgresql.mapper;

import com.nttdata.dockerized.postgresql.model.dto.ProductCreateRequestDto;
import com.nttdata.dockerized.postgresql.model.dto.ProductResponseDto;
import com.nttdata.dockerized.postgresql.model.dto.ProductUpdateRequestDto;
import com.nttdata.dockerized.postgresql.model.entity.Category;
import com.nttdata.dockerized.postgresql.model.entity.Product;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;
@Mapper(componentModel = "spring")
public interface ProductMapper {

    // CREATE → de DTO a entidad
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true) // se setea en el service
    @Mapping(target = "detallesPedido", ignore = true)
    Product toEntity(ProductCreateRequestDto dto);

    // UPDATE → aplicamos solo los campos no nulos
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true) // se maneja en el service
    @Mapping(target = "detallesPedido", ignore = true)
    void updateEntityFromDto(ProductUpdateRequestDto dto, @MappingTarget Product entity);

    // RESPONSE → de entidad a DTO
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    ProductResponseDto toResponseDto(Product product);

    List<ProductResponseDto> toResponseList(List<Product> products);
}
