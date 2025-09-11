package cd.dev.productms.service;

import cd.dev.productms.model.dto.CategoryCreateRequestDto;
import cd.dev.productms.model.dto.CategoryResponseDto;
import cd.dev.productms.model.dto.CategoryUpdateRequestDto;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDto> listAll();

    CategoryResponseDto findById(Long id);

    CategoryResponseDto save(CategoryCreateRequestDto request);

    CategoryResponseDto update(Long id, CategoryUpdateRequestDto request);

    void deleteById(Long id);
}
