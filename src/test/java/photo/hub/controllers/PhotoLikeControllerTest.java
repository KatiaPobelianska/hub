package photo.hub.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PhotoLikeControllerTest {
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
    @WithMockUser
    @DisplayName("Get the number of likes by postId")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void countOfLikeByPost_validPostId_Ok() throws Exception {
        //Given
        long postId = 1L;

        //When
        MvcResult result = mockMvc.perform(
                        get("/likes")
                                .param("postId", Long.toString(postId))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        int actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), new TypeReference<>() {
        });

        //Then
        assertEquals(2, actual);
    }

    @Test
    @WithUserDetails("Joe")
    @DisplayName("Is the like by user present in selected post test")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void isLikePresent_validPostId_Ok() throws Exception {
        //Given
        long postId = 1L;

        //Then
        mockMvc.perform(
                        get("/likes/present")
                                .param("postId", Long.toString(postId))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("Bob")
    @DisplayName("Add like on the post with specified id")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addLike_validPostId_Ok() throws Exception {
        //Given
        long postId = 1L;

        //Then
        mockMvc.perform(
                        post("/likes")
                                .param("postId", Long.toString(postId))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @WithUserDetails("Bob")
    @DisplayName("Add like on the post with specified id - invalid ID")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addLike_InvalidPostId_NotOk() throws Exception {
        //Given
        long postId = 101L;

        //When
        mockMvc.perform(
                        post("/likes")
                                .param("postId", Long.toString(postId))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("Bob")
    @DisplayName("Delete like from post by id")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void delete_validPostId_Ok() throws Exception {
        //Given
        long postId = 3L;

        //When
        mockMvc.perform(
                        delete("/likes")
                                .param("postId", Long.toString(postId))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("Bob")
    @DisplayName("Delete like from post by id with invalid Id")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void delete_InvalidPostId_NotOk() throws Exception {
        //Given
        long postId = 1L;

        //When
        mockMvc.perform(
                        delete("/likes")
                                .param("postId", Long.toString(postId))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }
}
