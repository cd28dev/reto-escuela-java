package cd.dev.compositionorder.dto;


import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class OrderCompositionCreateRequestDto {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @NotEmpty(message = "Los productos son obligatorios")
    private List<ProductOrderDto> productos;

    private String observaciones;
}