package no.ntnu.microService.controller.goal;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.microService.model.DTO.MessageResponse;
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

  @GetMapping("testGetUser")
  public String testGetUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    return "TestGetUser: " + username;
  }

  @PostMapping("setGoal")
  public String setUser(@RequestBody GoalRequest goal) {
    MessageResponse response = goalService.setGoal(goal);
    return response.getMessage();
  }

  @GetMapping("getActiveGoal")
  public Goal getActiveGoal() {
    Optional<Goal> goal = goalService.getActiveGoal();
    return goal.orElse(new Goal());
  }
}
