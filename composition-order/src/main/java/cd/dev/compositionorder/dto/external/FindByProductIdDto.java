package cd.dev.compositionorder.dto.external;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FindByProductIdDto {
    private Long productId;
    private Integer total;
}
