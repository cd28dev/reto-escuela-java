package com.nttdata.dockerized.postgresql.mapper;

import com.nttdata.dockerized.postgresql.model.dto.ProductSaveRequestDto;
import com.nttdata.dockerized.postgresql.model.dto.ProductSaveResponseDto;
import com.nttdata.dockerized.postgresql.model.entity.Category;
import com.nttdata.dockerized.postgresql.model.entity.Product;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

@Mapper(uses = {CategoryMapper.class})
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    public ProductDto map(Product product);
    public List<ProductDto> map(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "detallesPedido", ignore = true)
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "categoryIdToCategory")
    public Product toEntity(ProductSaveRequestDto productSaveRequestDto);

    public ProductSaveResponseDto toProductSaveResponseDto(Product product);

    default Boolean mapActiveStringToBoolean(String active) {
        return "Active".equalsIgnoreCase(active);
    }

    default Product toEntityForUpdate(Long id, ProductSaveRequestDto dto, Product existingProduct) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO de actualización no puede ser null");
        }
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser null");
        }

        Product product = new Product();
        product.setId(id);
        product.setDetallesPedido(existingProduct.getDetallesPedido());

        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            product.setName(dto.getName());
        } else {
            product.setName(existingProduct.getName());
        }

        if (dto.getDescription() != null && !dto.getDescription().trim().isEmpty()) {
            product.setDescription(dto.getDescription());
        } else {
            product.setDescription(existingProduct.getDescription());
        }

        if (dto.getPrice() != null && dto.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            product.setPrice(dto.getPrice());
        } else {
            product.setPrice(existingProduct.getPrice());
        }

        if (dto.getActive() != null) {
            product.setActive(mapActiveStringToBoolean(dto.getActive()));
        } else {
            product.setActive(existingProduct.getActive());
        }

        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            product.setCategory(category);
        } else {
            product.setCategory(existingProduct.getCategory());
        }

        return product;
    }

    @Named("categoryIdToCategory")
    default Category categoryIdToCategory(Long categoryId) {
        if (categoryId == null) return null;
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    @AfterMapping
    default void setRemainingValues(Product product, @MappingTarget ProductDto productDto) {
        productDto.setActive(Boolean.TRUE.equals(product.getActive()) ? "Active" : "Inactive");
    }

    @AfterMapping
    default void setRemainingValuesResponse(Product product, @MappingTarget ProductSaveResponseDto responseDto) {
        responseDto.setActive(Boolean.TRUE.equals(product.getActive()) ? "Active" : "Inactive");
    }
}
