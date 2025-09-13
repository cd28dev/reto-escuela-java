package cd.dev.compositionorder.dto.external;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PedidoResponseDto {
    private Long id;
    private LocalDateTime fechaPedido;
    private Boolean active;
    private Long id_user;
    private List<DetallePedidoResponseDto> detalles;
    private BigDecimal total;

}