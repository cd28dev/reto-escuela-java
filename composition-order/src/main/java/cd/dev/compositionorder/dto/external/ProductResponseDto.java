package cd.dev.compositionorder.dto.external;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean active;
    private Long categoryId;
    private String categoryName;

    @Getter
    @Setter
    public static class CategoryResponseDto {
        private Long id;
        private String name;
        private String description;
        private Boolean active;
    }
}