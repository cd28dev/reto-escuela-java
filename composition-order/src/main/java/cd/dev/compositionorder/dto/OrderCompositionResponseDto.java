package cd.dev.compositionorder.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompositionResponseDto {

    private Long pedidoId;
    private UserSummaryDto usuario;
    private LocalDateTime fechaPedido;
    private BigDecimal total;
    private List<ProductDetailDto> productosDetalle;
    private String status;

    @Data
    public static class UserSummaryDto {
        private Long id;
        private String name;
        private String email;
    }

    @Data
    public static class ProductDetailDto {
        private Long productId;
        private String productName;
        private BigDecimal precioUnitario;
        private Integer cantidad;
        private BigDecimal subtotal;
    }
}
