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
@Schema(description = "Response to the progress increase request")
public class IncreaseResponse {
  @Schema(description = "Amount by which the goal's progress was increased", example = "500.00")
  private BigDecimal amountIncreased;
  @Schema(description = "Indicates whether the goal has been completed after the increase",
      example = "false")
  private boolean goalComplete;
  @Schema(description = "Indicates if the progress increase was successful", example = "true")
  private boolean increaseSuccessful;
}
