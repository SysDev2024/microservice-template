package no.ntnu.microService.service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.microService.model.DTO.MessageResponse;
import no.ntnu.microService.model.DTO.SetGoalResponse;
import no.ntnu.microService.model.DTO.IncreaseResponse;
import no.ntnu.microService.model.DTO.goalDTO.GoalRequest;
import no.ntnu.microService.model.Goal.Goal;
import no.ntnu.microService.repository.GoalRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoalService {
  private final GoalRepository goalRepository;

  @Transactional
  public SetGoalResponse setGoal(GoalRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    var goal = Goal.builder()
                   .username(username)
                   .name(request.getName())
                   .description(request.getDescription())
                   .amount(request.getAmount())
                   .progress(new BigDecimal(0))
                   .active(true)
                   .deadline(request.getDeadline())
                   .completionTime(LocalDateTime.now())
                   .build();
    try {
      Optional<Goal> currentlyActiveGoal = getActiveGoal();
      if (currentlyActiveGoal.isPresent()) {
        currentlyActiveGoal.get().setActive(false);
        goalRepository.save(currentlyActiveGoal.get());
      }
      goalRepository.save(goal);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityViolationException("Goal already exists");
    }

    return new SetGoalResponse(true, goal.getId());
  }

  public Optional<Goal> getActiveGoal() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    return goalRepository.findActiveGoalByUser(username);
  }

  @Transactional
  public IncreaseResponse increaseProgress(BigDecimal amount) {
    Optional<Goal> goalwrapper = getActiveGoal();
    IncreaseResponse response = new IncreaseResponse();
    if (goalwrapper.isPresent()) {
      Goal goal = goalwrapper.get();
      goal.setProgress(goal.getProgress().add(amount));
      response.setAmountIncreased(amount);
      if (goal.getAmount().compareTo(goal.getProgress()) <= 0) {
        goal.setActive(false);
        response.setGoalComplete(true);
      }

      goalRepository.save(goal);
      response.setIncreaseSuccessful(true);
    }

    return response;
  }

  public List<Goal> getAllGoals() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    return goalRepository.findAllByUsername(username);
  }
}
