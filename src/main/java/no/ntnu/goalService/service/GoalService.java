package no.ntnu.microService.service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;
import no.ntnu.microService.model.DTO.MessageResponse;
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
  public MessageResponse setGoal(GoalRequest request) {
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
                   .build();
    try {
      System.out.println(goal.getDeadline());
      Optional<Goal> currentlyActiveGoal = getActiveGoal();
      if (currentlyActiveGoal.isPresent()) {
        currentlyActiveGoal.get().setActive(false);
        goalRepository.save(currentlyActiveGoal.get());
      }
      goalRepository.save(goal);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityViolationException("Goal already exists");
    }

    return MessageResponse.builder().message("Goal registered." + goal.getId().toString()).build();
  }

  public Optional<Goal> getActiveGoal() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    return goalRepository.findActiveGoalByUser(username);
  }

  // @Transactional
  // public AuthResponse verifyEmail(String token) {
  //
  //     var verificationToken = emailverificationTokenService.getVerificationToken(token);
  //
  //     if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
  //         throw new IllegalStateException("Token expired");
  //     }
  //
  //     var user = userRepository.findById(verificationToken.getUser().getId())
  //             .orElseThrow(() -> new IllegalStateException("User not found"));
  //
  //     // Check if the user is already enabled
  //     if (user.isEnabled()) {
  //         throw new IllegalStateException("Email is already verified");
  //     }
  //
  //     user.setEnabled(true);
  //     userRepository.save(user);
  //     var jwtToken = jwtService.generateToken(user);
  //     return AuthResponse.builder().token(jwtToken).build();
  //
  // }
}
