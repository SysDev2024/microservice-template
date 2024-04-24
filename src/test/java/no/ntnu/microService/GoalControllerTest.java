package no.ntnu.microService;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import no.ntnu.microService.model.DTO.IncreaseRequest;
import no.ntnu.microService.model.DTO.goalDTO.GoalRequest;
import no.ntnu.microService.model.Goal.Goal;
import no.ntnu.microService.repository.GoalRepository;
import no.ntnu.microService.service.GoalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class GoalControllerTest {
  @Autowired GoalRepository goalRepository;
  @Autowired GoalService goalService;
  @Autowired private MockMvc mockMvc;

  private static String username = "test1";
  static GoalRequest goal1 = GoalRequest.builder()
                                 .name("Read 5 books")
                                 .description("Goal to read 5 books this month")
                                 .amount(new BigDecimal("5"))
                                 .deadline(LocalDate.now().plusDays(30))
                                 .build();

  static GoalRequest goal2 = GoalRequest.builder()
                                 .name("Read 3 books")
                                 .description("Goal to read 3 books this month")
                                 .amount(new BigDecimal("3"))
                                 .deadline(LocalDate.now().plusDays(31))
                                 .build();
  static GoalRequest goal3 = GoalRequest.builder()
                                 .name("Read 1 books")
                                 .description("Goal to read 3 books this month")
                                 .amount(new BigDecimal("1"))
                                 .deadline(LocalDate.now().plusDays(31))
                                 .build();

  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  void setup() {
    objectMapper.registerModule(new JavaTimeModule());
    goalRepository.deleteAll();
  }

  private static String api = "/goal";

  @Test
  @WithMockUser(username = "test1", roles = {"USER"})
  void testGetActiveGoalWithEmptyDatabase() throws Exception {
    mockMvc.perform(get(api + "/getActiveGoal")).andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "test1", roles = {"USER"})
  void testHello() throws Exception {
    mockMvc.perform(get(api + "/Hello")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "test1", roles = {"USER"})
  void testGetActiveGoalWithOneGoalInDatabase() throws Exception {
    goalService.setGoal(goal1);
    mockMvc.perform(get(api + "/getActiveGoal"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Read 5 books"));
  }

  @Test
  @WithMockUser(username = "test1", roles = {"USER"})
  void testGetActiveGoalWithTwoGoalsInDatabase() throws Exception {
    goalService.setGoal(goal1);
    goalService.setGoal(goal2);
    mockMvc.perform(get(api + "/getActiveGoal"))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Read 3 books"));
  }

  @Test
  @WithMockUser(username = "test1", roles = {"USER"})
  void testIncreaseProgressWithNoActiveGoal() throws Exception {
    IncreaseRequest request = new IncreaseRequest(new BigDecimal("1"));
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonRequest = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(post(api + "/increaseProgress")
                     .contentType(MediaType.APPLICATION_JSON)
                     .content(jsonRequest))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.increaseSuccessful").value(false));
  }
  @Test
  @WithMockUser(username = "test1", roles = {"USER"})
  void testIncreaseProgressWithActiveGoal() throws Exception {
    goalService.setGoal(goal1);
    IncreaseRequest request = new IncreaseRequest(new BigDecimal("1"));
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonRequest = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(post(api + "/increaseProgress")
                     .contentType(MediaType.APPLICATION_JSON)
                     .content(jsonRequest))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.increaseSuccessful").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.amountIncreased").value(new BigDecimal(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.goalComplete").value(false));
  }

  @Test
  @WithMockUser(username = "test1", roles = {"USER"})
  void testIncreaseProgressWithNearlyCompleteGoal() throws Exception {
    goalService.setGoal(goal3);
    IncreaseRequest request = new IncreaseRequest(new BigDecimal("1"));
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonRequest = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(post(api + "/increaseProgress")
                     .contentType(MediaType.APPLICATION_JSON)
                     .content(jsonRequest))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.increaseSuccessful").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.amountIncreased").value(new BigDecimal(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.goalComplete").value(true));
  }

  @Test
  @WithMockUser(username = "test1", roles = {"USER"})
  void testIncreaseProgressCompletedGoalIsNotActive() throws Exception {
    goalService.setGoal(goal3);
    IncreaseRequest request = new IncreaseRequest(new BigDecimal("1"));
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonRequest = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(post(api + "/increaseProgress")
                     .contentType(MediaType.APPLICATION_JSON)
                     .content(jsonRequest))
        .andExpect(status().isOk());

    mockMvc
        .perform(post(api + "/increaseProgress")
                     .contentType(MediaType.APPLICATION_JSON)
                     .content(jsonRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.increaseSuccessful").value(false));
  }

  @Test
  @WithMockUser(username = "test1", roles = {"USER"})
  void testGetAllGoals() throws Exception {
    goalService.setGoal(goal1);
    goalService.setGoal(goal2);
    mockMvc.perform(get(api + "/getAllGoals"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name", is("Read 5 books")))
        .andExpect(jsonPath("$[1].name", is("Read 3 books")));
  }

  @Test
  @WithMockUser(username = "test1", roles = {"USER"})
  void testSetGoal() throws Exception {
    String jsonRequest = objectMapper.writeValueAsString(goal1);

    mockMvc
        .perform(
            post(api + "/setGoal").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
        .andExpect(status().isOk());

    mockMvc.perform(get(api + "/getActiveGoal"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Read 5 books"));
  }
}
