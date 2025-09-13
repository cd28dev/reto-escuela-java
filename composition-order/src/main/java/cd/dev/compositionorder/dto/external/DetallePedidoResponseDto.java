package cd.dev.compositionorder.dto.external;


import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class DetallePedidoResponseDto {
    private Long id;
    private Long productoId;
    private String productoName;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}