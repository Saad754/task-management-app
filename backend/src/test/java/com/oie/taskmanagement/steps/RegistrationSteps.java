package com.oie.taskmanagement.steps;

import com.oie.taskmanagement.dto.RegisterRequest;
import com.oie.taskmanagement.exception.DuplicateResourceException;
import com.oie.taskmanagement.repository.UserRepository;
import com.oie.taskmanagement.service.AuthService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class RegistrationSteps {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;

    private Exception thrownException;
    private boolean registrationSucceeded;

    @Before
    public void cleanDatabase() {
        thrownException = null;
        registrationSucceeded = false;
    }

    @Given("no users are registered")
    public void noUsersAreRegistered() {
        userRepository.deleteAll();
    }
    @Given("a user is registered with email {string}")
    public void aUserIsRegisteredWithEmail(String email) {
        authService.register(new RegisterRequest("saad", email, "testpass"));
    }
    @Given("a user is registered with username {string}")
    public void aUserIsRegisteredWithUsername(String username) {
        authService.register(new RegisterRequest(username, "saad@gmail.com", "testpass"));
    }
    @When("someone registers with username {string} and email {string}")
    public void someoneRegistersWith(String username, String email) {
        try {
            authService.register(new RegisterRequest(username, email, "testpass"));
            registrationSucceeded = true;
        } catch (DuplicateResourceException e) {
            thrownException = e;
        }
    }
    @Then("the registration is accepted")
    public void theRegistrationIsAccepted() {
        assertTrue(registrationSucceeded);
    }
    @Then("the registration is rejected")
    public void theRegistrationIsRejected() {
        assertInstanceOf(DuplicateResourceException.class, thrownException);
    }
    @Then("the error says the email is already in use")
    public void theErrorSaysTheEmailIsAlreadyInUse() {
        assertEquals("Email already in use", thrownException.getMessage());
    }
    @Then("the error says the username is already in use")
    public void theErrorSaysTheUsernameIsAlreadyInUse() {
        assertEquals("Username already taken", thrownException.getMessage());
    }
}