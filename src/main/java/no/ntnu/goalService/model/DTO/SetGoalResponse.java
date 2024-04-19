package no.ntnu.microService.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response to a successful goal setting operation")
public class SetGoalResponse {
  @Schema(description = "Indicates if the operation was successful") private boolean successful;
  @Schema(description = "The unique identifier of the newly set goal") private UUID id;
}
