package cd.dev.productms.mapper;

import java.util.List;

import cd.dev.productms.model.dto.ProductCreateRequestDto;
import cd.dev.productms.model.dto.ProductResponseDto;
import cd.dev.productms.model.dto.ProductUpdateRequestDto;
import cd.dev.productms.model.entity.Product;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface ProductMapper {

    // CREATE → de DTO a entidad
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductCreateRequestDto dto);

    // UPDATE → aplicamos solo los campos no nulos
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntityFromDto(ProductUpdateRequestDto dto, @MappingTarget Product entity);

    // RESPONSE → de entidad a DTO
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    ProductResponseDto toResponseDto(Product product);

    List<ProductResponseDto> toResponseList(List<Product> products);
}