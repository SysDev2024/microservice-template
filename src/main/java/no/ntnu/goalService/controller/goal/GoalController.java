package no.ntnu.microService.controller.goal;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.microService.model.DTO.IncreaseRequest;
import no.ntnu.microService.model.DTO.IncreaseResponse;
import no.ntnu.microService.model.DTO.MessageResponse;
import no.ntnu.microService.model.DTO.SetGoalResponse;
import no.ntnu.microService.model.DTO.goalDTO.GoalRequest;
import no.ntnu.microService.model.Goal.Goal;
import no.ntnu.microService.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/goal")
@RequiredArgsConstructor
@Slf4j
public class GoalController {
  @Autowired private GoalService goalService;

  @GetMapping("Hello")
  public String hello() {
    return "Hello";
  }

  @PostMapping("setGoal")
  public ResponseEntity<SetGoalResponse> setGoal(@RequestBody GoalRequest goal) {
    return ResponseEntity.ok(goalService.setGoal(goal));
  }

  @GetMapping("getActiveGoal")
  public ResponseEntity<Goal> getActiveGoal() {
    Optional<Goal> goal = goalService.getActiveGoal();
    return goal.map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
  }

  @PostMapping("increaseProgress")
  public ResponseEntity<IncreaseResponse> increaseProgress(@RequestBody IncreaseRequest increase) {
    return ResponseEntity.ok(goalService.increaseProgress(increase.getAmount()));
  }

  @GetMapping("getAllGoals")
  public ResponseEntity<List<Goal>> getAllGoals() {
    return ResponseEntity.ok(goalService.getAllGoals());
  }
}
