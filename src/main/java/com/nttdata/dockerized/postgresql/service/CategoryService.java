package com.nttdata.dockerized.postgresql.service;

import com.nttdata.dockerized.postgresql.model.dto.*;

import java.util.List;

public interface CategoryService {

    List<CategoryResponseDto> listAll();

    CategoryResponseDto findById(Long id);

    CategoryResponseDto save(CategoryCreateRequestDto request);

    CategoryResponseDto update(Long id, CategoryUpdateRequestDto request);

    void deleteById(Long id);
}
