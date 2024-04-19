package no.ntnu.microService.controller.goal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/goal")
@RequiredArgsConstructor
@Slf4j
public class GoalController {
  @Autowired private GoalService goalService;

  @Operation(summary = "Get a greeting", description = "Returns a simple hello message")
  @GetMapping("Hello")
  public String hello() {
    return "Hello";
  }

  @Operation(summary = "Set a new goal",
      description =
          "Creates a new goal for the authenticated user and deactivates any currently active goal.",
      security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponse(responseCode = "200", description = "Goal successfully set",
      content = @Content(schema = @Schema(implementation = SetGoalResponse.class)))
  @ApiResponse(responseCode = "400", description = "Invalid input data")
  @ApiResponse(responseCode = "500", description = "Internal server error")
  @PostMapping("setGoal")
  public ResponseEntity<SetGoalResponse>
  setGoal(@Parameter(description = "Request body for setting a new goal",
      required = true) @RequestBody GoalRequest goal) {
    return ResponseEntity.ok(goalService.setGoal(goal));
  }

  @Operation(summary = "Get active goal",
      description = "Retrieves the currently active goal for the authenticated user.",
      security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponse(responseCode = "200", description = "Active goal found",
      content = @Content(schema = @Schema(implementation = Goal.class)))
  @ApiResponse(responseCode = "400", description = "No active goal available")
  @GetMapping("getActiveGoal")
  public ResponseEntity<Goal>
  getActiveGoal() {
    Optional<Goal> goal = goalService.getActiveGoal();
    return goal.map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
  }

  @Operation(summary = "Increase goal progress",
      description =
          "Increases the progress of the currently active goal of an authenticated user by a specified amount.",
      security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponse(responseCode = "200", description = "Progress increased successfully",
      content = @Content(schema = @Schema(implementation = IncreaseResponse.class)))
  @ApiResponse(responseCode = "400", description = "Invalid input or unable to increase progress")
  @PostMapping("increaseProgress")
  public ResponseEntity<IncreaseResponse>
  increaseProgress(@Parameter(description = "Details of the amount to increase",
      required = true) @RequestBody IncreaseRequest increase) {
    return ResponseEntity.ok(goalService.increaseProgress(increase.getAmount()));
  }

  @Operation(summary = "Retrieve all goals",
      description = "Fetches a list of all goals stored in the system.",
      security = @SecurityRequirement(name = "bearerAuth"))

  @ApiResponse(responseCode = "200", description = "List of all goals for the authenticated user",
      content = @Content(array = @ArraySchema(schema = @Schema(implementation = Goal.class))))
  @GetMapping("getAllGoals")
  public ResponseEntity<List<Goal>>
  getAllGoals() {
    return ResponseEntity.ok(goalService.getAllGoals());
  }
}
