package cd.dev.stockms.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockCreateRequestDto {

    @NotNull(message = "El productId es obligatorio")
    private Long productId;

    @NotNull(message = "El warehouseId es obligatorio")
    private Long wareHouseId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer quantity;
}
