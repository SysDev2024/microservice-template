package no.ntnu.microService.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request to increase the goal's progress")
public class IncreaseRequest {
  @Schema(description = "Amount to increase the goal's progress by", required = true,
      example = "200.00")
  private BigDecimal amount;
}
