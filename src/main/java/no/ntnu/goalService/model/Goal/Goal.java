package no.ntnu.microService.model.Goal;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.ntnu.microService.model.User.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "goal")
public class Goal {
  @Schema(description = "Unique identifier of the goal",
      example = "123e4567-e89b-12d3-a456-426614174000")
  @Id
  @GeneratedValue
  private UUID id;

  @Schema(description = "Name of the goal", example = "Save for a new car") private String name;
  @Schema(description = "Deadline to achieve the goal", example = "2024-12-31")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate deadline;
  @Schema(description = "Time when the goal was completed", example = "2023-10-05T14:48:00")
  private LocalDateTime completionTime;
  @Schema(description = "Indicates whether the goal is currently active", example = "true")
  private boolean active;
  @Schema(
      description = "Description of the goal", example = "Goal to save money for buying a new car")
  private String description;
  @Schema(description = "Target amount for the goal", example = "20000.00")
  private BigDecimal amount;
  @Schema(description = "Current progress towards the goal", example = "15000.00")
  private BigDecimal progress;
  @Schema(description = "Username of the goal owner", example = "user123") private String username;

  public UUID getId() {
    return id;
  }
}
