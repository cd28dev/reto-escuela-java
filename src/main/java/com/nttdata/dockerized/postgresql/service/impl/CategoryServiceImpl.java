package com.nttdata.dockerized.postgresql.service.impl;

import com.nttdata.dockerized.postgresql.exception.BadRequestException;
import com.nttdata.dockerized.postgresql.exception.CustomBadRequestException;
import com.nttdata.dockerized.postgresql.exception.NotFoundException;
import com.nttdata.dockerized.postgresql.mapper.CategoryMapper;
import com.nttdata.dockerized.postgresql.model.dto.CategoryCreateRequestDto;
import com.nttdata.dockerized.postgresql.model.dto.CategoryResponseDto;
import com.nttdata.dockerized.postgresql.model.dto.CategoryUpdateRequestDto;
import com.nttdata.dockerized.postgresql.model.entity.Category;
import com.nttdata.dockerized.postgresql.repository.CategoryRepository;
import com.nttdata.dockerized.postgresql.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> listAll() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDtoList(categories);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + id));
        return categoryMapper.toResponseDto(category);
    }

    @Override
    public CategoryResponseDto save(CategoryCreateRequestDto request) {
        if (request == null) {
            throw new CustomBadRequestException("El request no puede ser nulo", 1000);
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new CustomBadRequestException("El nombre de la categoría no puede estar vacío", 1001);
        }

        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponseDto(savedCategory);
    }

    @Override
    public CategoryResponseDto update(Long id, CategoryUpdateRequestDto request) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + id));

        if (request == null) {
            throw new CustomBadRequestException("El request no puede ser nulo", 1000);
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new CustomBadRequestException("El nombre de la categoría no puede estar vacío", 1001);
        }

        categoryMapper.updateEntityFromDto(request, existingCategory);

        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.toResponseDto(updatedCategory);
    }

    @Override
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Categoría no encontrada con ID: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
