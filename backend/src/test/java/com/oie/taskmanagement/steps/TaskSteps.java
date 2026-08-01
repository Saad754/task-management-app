package com.oie.taskmanagement.steps;

import com.oie.taskmanagement.dto.CreateTaskRequest;
import com.oie.taskmanagement.dto.TaskResponse;
import com.oie.taskmanagement.dto.UpdateTaskRequest;
import com.oie.taskmanagement.entity.TaskPriority;
import com.oie.taskmanagement.entity.TaskStatus;
import com.oie.taskmanagement.entity.User;
import com.oie.taskmanagement.exception.ResourceNotFoundException;
import com.oie.taskmanagement.repository.TaskRepository;
import com.oie.taskmanagement.repository.UserRepository;
import com.oie.taskmanagement.service.TaskService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TaskSteps {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private TaskResponse createdTask;
    private List<TaskResponse> returnedTasks;
    private Exception thrownException;

    @Before
    public void reset() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        SecurityContextHolder.clearContext();
        createdTask = null;
        returnedTasks = null;
        thrownException = null;
    }
    private void loginAs(String username) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(username, null, List.of()));
    }
    private User createUser(String username) {
        return userRepository.save(new User(
                username,
                passwordEncoder.encode("testpassword"),
                username + "@gmail.com"));
    }
    @Given("a user {string} exists")
    public void aUserExists(String username) {
        createUser(username);
    }
    @Given("a user {string} owns a task titled {string}")
    public void aUserOwnsATaskTitled(String username, String title) {
        createUser(username);
        loginAs(username);
        taskService.createTask(new CreateTaskRequest(title, "test description", TaskPriority.MEDIUM));
    }
    @When("user {string} tries to create a task titled {string} with priority {string}")
    public void userTriesToCreateATask(String username, String title, String priority) {
        loginAs(username);
        createdTask = taskService.createTask(
                new CreateTaskRequest(title, "a description", TaskPriority.valueOf(priority)));
    }
    @When("user {string} tries to see his tasks list")
    public void userTriesToSeeHisTasksList(String username) {
        loginAs(username);
        returnedTasks = taskService.getTasks(null, null);
    }
    @When("user {string} tries to edit the task titled {string}")
    public void userTriesToEditTheTask(String username, String title) {
        Long taskId = taskRepository.findAll().stream()
                .filter(t -> t.getTitle().equals(title))
                .findFirst()
                .orElseThrow()
                .getId();

        loginAs(username);
        try {
            taskService.updateTask(taskId, new UpdateTaskRequest(
                    "edited title", "edited", TaskPriority.LOW, TaskStatus.DONE));
        } catch (ResourceNotFoundException e) {
            thrownException = e;
        }
    }
    @When("user {string} tries to delete the task titled {string}")
    public void userTriesToDeleteTheTask(String username, String title) {
        Long taskId = taskRepository.findAll().stream()
                .filter(t -> t.getTitle().equals(title))
                .findFirst()
                .orElseThrow()
                .getId();

        loginAs(username);
        try {
            taskService.deleteTask(taskId);
        } catch (ResourceNotFoundException e) {
            thrownException = e;
        }
    }
    @Then("the task is created with status {string}")
    public void theTaskIsCreatedWithStatus(String status) {
        assertNotNull(createdTask);
        assertEquals(TaskStatus.valueOf(status), createdTask.status());
    }
    @Then("only tasks belonging to {string} are returned")
    public void onlyTasksBelongingToAreReturned(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        assertEquals(1, returnedTasks.size());

        boolean allOwned = taskRepository.findByUserId(user.getId()).size() == returnedTasks.size();
        assertTrue(allOwned);
    }
    @Then("the edit request is rejected")
    public void theEditRequestIsRejected() {
        assertNotNull(thrownException);
    }
    @Then("the delete request is rejected")
    public void theDeleteRequestIsRejected() {
        assertNotNull(thrownException);
    }
}