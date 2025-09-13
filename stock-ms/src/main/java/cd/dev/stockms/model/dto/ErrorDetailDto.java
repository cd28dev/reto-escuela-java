package cd.dev.stockms.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorDetailDto {
    private String message;
    private LocalDateTime dateTime;
}
