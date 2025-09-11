package cd.dev.productms.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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