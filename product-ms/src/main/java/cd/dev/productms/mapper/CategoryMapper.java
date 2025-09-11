package cd.dev.productms.mapper;


import cd.dev.productms.model.dto.CategoryCreateRequestDto;
import cd.dev.productms.model.dto.CategoryResponseDto;
import cd.dev.productms.model.dto.CategoryUpdateRequestDto;
import cd.dev.productms.model.entity.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    Category toEntity(CategoryCreateRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CategoryUpdateRequestDto dto, @MappingTarget Category category);

    CategoryResponseDto toResponseDto(Category category);

    List<CategoryResponseDto> toDtoList(List<Category> categories);
}