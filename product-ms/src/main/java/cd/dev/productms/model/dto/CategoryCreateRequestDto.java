package cd.dev.productms.model.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreateRequestDto {
    @NotBlank
    private String name;
}