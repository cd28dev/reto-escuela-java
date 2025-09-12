package cd.dev.compositionorder.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class OrderCompositionRequestDto {
    @NotNull(message = "User ID es requerido")
    private Long userId;

    @NotEmpty(message = "Debe incluir al menos un producto")
    @Valid
    private List<ProductOrderDto> productos;
}
