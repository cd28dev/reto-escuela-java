package com.nttdata.dockerized.postgresql.mapper;

import com.nttdata.dockerized.postgresql.model.dto.*;
import org.mapstruct.*;
import com.nttdata.dockerized.postgresql.model.entity.Category;

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
