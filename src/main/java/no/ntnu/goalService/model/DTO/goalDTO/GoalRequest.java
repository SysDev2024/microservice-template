package no.ntnu.microService.model.DTO.goalDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class GoalRequest {
  private String name;
  @JsonFormat(pattern = "yyyy-MM-dd") private LocalDate deadline;
  private String description;
  private BigDecimal amount;
}
