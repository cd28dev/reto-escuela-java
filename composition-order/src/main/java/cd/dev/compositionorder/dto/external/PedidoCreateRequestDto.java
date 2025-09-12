package cd.dev.compositionorder.dto.external;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class PedidoCreateRequestDto {
    @NotNull
    private Long userId;

    private Boolean active = true;
}