package photo.hub.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import photo.hub.model.Person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerTest {

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
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GetAllNotAdmin users")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllNotAdmin_ValidRole_Ok() throws Exception {

        //When
        MvcResult result = mockMvc.perform(get("/admin/allNotAdmin")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        List<Person> actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), new TypeReference<>() {
        });

        //Then
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    @WithMockUser()
    @DisplayName("GetAllNotAdmin with invalid role")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllNotAdmin_InValidRole_NotOk() throws Exception {
        //When
        mockMvc.perform(get("/admin/allNotAdmin")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Add new admin to DB")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addAdmin_ValidRole_Ok() throws Exception {
        //Given
        Long id = 1L;

        //Then
        mockMvc.perform(patch("/admin/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser()
    @DisplayName("Add admin with invalid role")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addAdmin_InValidRole_NotOk() throws Exception {
        //Given
        Long id = 1L;
        //Then
        mockMvc.perform(patch("/admin/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Add admin: Invalid user id")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addAdmin_InValidUserId_NotOk() throws Exception {
        //Given
        Long id = 100L;
        //Then
        mockMvc.perform(patch("/admin/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }
}
