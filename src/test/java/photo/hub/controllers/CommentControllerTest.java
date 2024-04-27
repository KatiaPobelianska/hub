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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import photo.hub.dto.CommentInputDto;
import photo.hub.model.Comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {
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
    @WithUserDetails("Bob")
    @DisplayName("Add comment, valid request")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addComment_ValidRequest_Ok() throws Exception {
        //Given
        CommentInputDto commentRequest = new CommentInputDto()
                .setPostId(1L)
                .setDescription("Insane post, Bob!");
        String jsonRequest = objectMapper.writeValueAsString(commentRequest);

        //When
        mockMvc.perform(
                        post("/comments")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails("Bob")
    @DisplayName("Add comment, Invalid request")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addComment_InValidRequest_Ok() throws Exception {
        //Given
        CommentInputDto commentRequest = new CommentInputDto()
                .setPostId(101L)
                .setDescription("Insane post, Bob!");
        String jsonRequest = objectMapper.writeValueAsString(commentRequest);

        //When
        mockMvc.perform(
                        post("/comments")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("Bob")
    @DisplayName("Get all comments by post")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllByPost_validPostId_Ok() throws Exception {
        //Given
        long postId = 1L;

        //When
        MvcResult result = mockMvc.perform(
                        get("/comments")
                                .param("postId", Long.toString(postId))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<Comment> actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), new TypeReference<>() {
        });

        //Then
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    @WithMockUser
    @DisplayName("Get all comments by post with invalid id")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllByPost_InvalidPostId_Ok() throws Exception {
        //Given
        long postId = 101L;

        //Then
        mockMvc.perform(
                        get("/comments")
                                .param("postId", Long.toString(postId))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("Get all comments by username, expected result")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllByUsername_validAuthorName_Ok() throws Exception {
        //Given
        String username = "Joe";

        //When
        MvcResult result = mockMvc.perform(
                        get("/comments/all")
                                .param("username", username)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<Comment> actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), new TypeReference<>() {
        });

        //Then
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }
}
