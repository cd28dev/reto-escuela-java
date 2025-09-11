package cd.dev.productms.controller;


import cd.dev.productms.model.dto.CategoryCreateRequestDto;
import cd.dev.productms.model.dto.CategoryResponseDto;
import cd.dev.productms.model.dto.CategoryUpdateRequestDto;
import cd.dev.productms.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponseDto> getAllCategories() {
        return categoryService.listAll();
    }

    @GetMapping("/{id}")
    public CategoryResponseDto getCategoryById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PostMapping
    public CategoryResponseDto createCategory(@RequestBody CategoryCreateRequestDto request) {
        return categoryService.save(request);
    }

    @PutMapping("/{id}")
    public CategoryResponseDto updateCategory(@PathVariable Long id, @RequestBody CategoryUpdateRequestDto request) {
        return categoryService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }
}