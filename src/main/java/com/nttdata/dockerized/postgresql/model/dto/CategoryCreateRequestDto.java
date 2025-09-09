package com.nttdata.dockerized.postgresql.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreateRequestDto {
    @NotBlank
    private String name;
}
