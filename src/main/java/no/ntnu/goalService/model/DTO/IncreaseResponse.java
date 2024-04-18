package no.ntnu.microService.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncreaseResponse {
  private BigDecimal amountIncreased;
  private boolean goalComplete;
  private boolean increaseSuccessful;
}
