package no.ntnu.microService.model.DTO.goalDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.ntnu.microService.model.User.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request body for setting a goal")
public class GoalRequest {
  @Schema(description = "Name of the goal", example = "Save for vacation") private String name;
  @Schema(description = "Deadline for the goal", example = "2024-12-31")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate deadline;
  @Schema(
      description = "Description of the goal", example = "Description of what the goal is about")
  private String description;
  @Schema(description = "Target amount for the goal", example = "5000.00")
  private BigDecimal amount;
}
