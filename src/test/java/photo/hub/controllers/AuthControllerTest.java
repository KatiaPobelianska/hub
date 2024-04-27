package photo.hub.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import photo.hub.dto.LoginRequest;
import photo.hub.dto.PersonDto;
import photo.hub.jwt.JwtAuthenticationResponse;
import photo.hub.model.Person;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource,
                          @Autowired WebApplicationContext applicationContext) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/beforeAllTests.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/after/after_afterTestExecution.sql")
            );
        }
    }

    @Test
    @DisplayName("PerformRegistration: valid request")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void performRegistration_ValidUserData_Ok() throws Exception {
        //Given
        PersonDto request = createRegistrationRequest();
        Person expected = getResponse(request);
        String jsonRequest = objectMapper.writeValueAsString(request);
        //When
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/auth/registration")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        Person actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), Person.class);
        expected.setPassword(actual.getPassword());
        //Then
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PerformRegistration: already existing user")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void performRegistration_existingUser_NotOk() throws Exception {
        //Given
        PersonDto request = createRegistrationRequest();
        request.setUsername("Bob")
                .setEmail("bob@mail.com")
                .setPassword("bob12345");
        String jsonRequest = objectMapper.writeValueAsString(request);
        //When
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/auth/registration")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PerformRegistration: invalid password length(min-8)")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void performRegistration_InvalidPassword_NotOk() throws Exception {
        //Given
        PersonDto request = createRegistrationRequest();
        request.setPassword("bob123");
        String jsonRequest = objectMapper.writeValueAsString(request);
        //When
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/auth/registration")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Login: valid request")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void login_ValidRequest_Ok() throws Exception {
        //Given
        LoginRequest request = new LoginRequest()
                .setUsername("Bob")
                .setPassword("bob12345");
        String jsonRequest = objectMapper.writeValueAsString(request);
        //When
        MvcResult result = mockMvc.perform(
                        post("/auth/login")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        JwtAuthenticationResponse actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), JwtAuthenticationResponse.class);
        //Then
        assertNotNull(actual);
    }

    @Test
    @DisplayName("Login: Invalid request")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void login_InValidRequest_NotOk() throws Exception {
        //Given
        LoginRequest request = new LoginRequest()
                .setUsername("Unknown")
                .setPassword("unk12345");
        String jsonRequest = objectMapper.writeValueAsString(request);
        //Then
        mockMvc.perform(
                        post("/auth/login")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    private Person getResponse(PersonDto request) {
        Person person = new Person();
        person.setId(4L);
        person.setUsername(request.getUsername());
        person.setEmail(request.getEmail());
        person.setRole("ROLE_USER");
        person.setEnable(false);
        return person;
    }

    private PersonDto createRegistrationRequest() {
        return new PersonDto()
                .setUsername("Andry")
                .setEmail("andry@mail.com")
                .setPassword("andry12345");
    }
}
