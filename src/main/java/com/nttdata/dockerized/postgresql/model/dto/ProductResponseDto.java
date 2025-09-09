package com.nttdata.dockerized.postgresql.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductSaveResponseDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String active;
    private CategoryDto category;
}
