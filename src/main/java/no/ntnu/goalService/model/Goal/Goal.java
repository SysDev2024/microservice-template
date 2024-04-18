package no.ntnu.microService.model.Goal;

import com.fasterxml.jackson.annotation.JsonFormat;
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
  @Id @GeneratedValue private UUID id;

  private String name;
  @JsonFormat(pattern = "yyyy-MM-dd") private LocalDate deadline;
  // @JsonFormat(pattern = "yyyy-MM-dd") 
  private LocalDateTime completionTime;
  private boolean active;
  private String description;
  private BigDecimal amount;
  private BigDecimal progress;
  private String username;

  public UUID getId() {
    return id;
  }
}
