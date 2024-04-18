package no.ntnu.microService.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import no.ntnu.microService.model.Goal.Goal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, UUID> {
  @Query("SELECT g FROM Goal g WHERE g.username = :username AND g.active = true") 
  Optional<Goal> findActiveGoalByUser(@Param("username") String username);

  Optional<Goal> findByUsername(String username);

  Optional<Goal> findById(UUID id);

  List<Goal> findAllByUsername(String username);
}
