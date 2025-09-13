package cd.dev.compositionorder.dto.external;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockResponseDto {
    private Long id;
    private Long productId;
    private Long wareHouseId;
    private Integer quantity;
}
