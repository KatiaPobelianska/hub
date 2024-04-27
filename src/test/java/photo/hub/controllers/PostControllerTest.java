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
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import photo.hub.dto.PostDtoOutput;
import photo.hub.model.Category;
import photo.hub.model.Post;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerTest {
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
    @DisplayName("Get post by id: valid post id")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getPostById_ValidId_Ok() throws Exception {
        //Given
        Long id = 1L;
        //When
        MvcResult result = mockMvc.perform(
                        get("/posts/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        PostDtoOutput actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), PostDtoOutput.class);
        //Then
        String expectedOwnerName = "Bob";
        assertNotNull(actual);
        assertEquals(expectedOwnerName, actual.getUsername());
    }

    @Test
    @WithMockUser
    @DisplayName("Get post by id: Invalid post id")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getPostById_InValidId_NotOk() throws Exception {
        //Given
        Long id = 100L;
        //Then
        mockMvc.perform(
                        get("/posts/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("Get all existing posts")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAll_Ok() throws Exception {
        //Given
        MvcResult result = mockMvc.perform(
                        get("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        List<PostDtoOutput> actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), new TypeReference<>() {
        });
        //Then
        assertNotNull(actual);
        assertEquals(6, actual.size());
    }

    @Test
    @WithUserDetails("Bob")
    @DisplayName("Get all  posts by user")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllPostsByUser_Ok() throws Exception {
        //Given
        MvcResult result = mockMvc.perform(
                        get("/posts/user")
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isOk())
                .andReturn();
        List<PostDtoOutput> actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), new TypeReference<>() {
        });
        //Then
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    @WithMockUser
    @DisplayName("Get all posts by title key")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findPostsByKey_ExistingKey_Ok() throws Exception {
        //Given
        String key = "Java";
        //When
        MvcResult result = mockMvc.perform(
                        get("/posts/find/{key}", key)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        List<Post> actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), new TypeReference<>() {
        });
        //Then
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    @WithUserDetails("Bob")
    @DisplayName("Save new post")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void savePost_Ok() throws Exception {
        // Given
        Resource resource = new ClassPathResource("/Successful conversion.jpg");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                resource.getFilename(),
                "image/jpeg",
                resource.getInputStream()
        );

        // When
        mockMvc.perform(multipart("/posts")
                        .file(file)
                        .param("description", "description")
                        .param("title", "title")
                        .param("category", "OTHER")
                )
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @WithUserDetails("Bob")
    @DisplayName("Update post - return post with updated props")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updatePost_Ok() throws Exception {
        //Given
        PostDtoOutput request = createPostUpdateRequest();

        String jsonRequest = objectMapper.writeValueAsString(request);
        //When
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.patch("/posts/update")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        PostDtoOutput actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), PostDtoOutput.class);
        request.setCreatedAt(actual.getCreatedAt());
       //Then
        assertNotNull(actual);
        assertEquals(request, actual);
    }

    @Test
    @WithUserDetails("Joe")
    @DisplayName("Update post - invalid user")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updatePost_InvalidUserAccess_NotOk() throws Exception {
        //Given
        PostDtoOutput request = createPostUpdateRequest();

        String jsonRequest = objectMapper.writeValueAsString(request);
        //When
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/posts/update")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("Bob")
    @DisplayName("Update post - invalid id")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updatePost_InvalidPostIdNotOk() throws Exception {
        //Given
        PostDtoOutput request = createPostUpdateRequest();
        request.setId(101L);

        String jsonRequest = objectMapper.writeValueAsString(request);
        //When
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/posts/update")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("Add view to the existing post")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addView_ExistingId_Ok() throws Exception {
        //Given
        Long id = 2L;
        //Then
        mockMvc.perform(
                        patch("/posts/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser
    @DisplayName("Add view with post invalid")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addView_NonExistingPostId_NotOk() throws Exception {
        //Given
        Long id = 1000L;
        //Then
        mockMvc.perform(
                        patch("/posts/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @WithUserDetails("Bob")
    @DisplayName("Delete post from DB")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void delete_ExistingId_Ok() throws Exception {
        //Given
        Long id = 1L;
        //Then
        mockMvc.perform(
                        delete("/posts/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithUserDetails("Bob")
    @DisplayName("Delete post with not existing id - not found - 404")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void delete_NonExistingId_Ok() throws Exception {
        //Given
        Long id = 1000L;
        //Then
        mockMvc.perform(
                        delete("/posts/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @WithUserDetails("Joe")
    @DisplayName("Delete post created by another user - forbidden - 403")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void delete_InvalidUser_NotOk() throws Exception {
        //Given
        Long id = 1L;
        //When
        mockMvc.perform(
                        delete("/posts/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithMockUser
    @DisplayName("GetFivePostsOfMostViews posts")
    @Sql(scripts = {
            "classpath:database/before/before_TestExecution.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/after/after_afterTestExecution.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getFivePostsOfMostViews_Ok() throws Exception {
        //Given
        int expectedTopViews = 1200;
        int expectedLowViews = 430;
        //When
        MvcResult result = mockMvc.perform(
                        get("/posts/best")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        List<Post> actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), new TypeReference<>() {
        });
        //Then
        assertNotNull(actual);
        assertEquals(5, actual.size());
        assertEquals(actual.get(0).getViews(), expectedTopViews);
        assertEquals(actual.get(4).getViews(), expectedLowViews);
    }

    PostDtoOutput createPostUpdateRequest() {
        return new PostDtoOutput()
                .setId(2L)
                .setTitle("Updated Title")
                .setDescription("Updated Description")
                .setUsername("Bob")
                .setCategory(Category.TECHNIC)
                .setPhotoUrl("someUrl")
                .setViews(1200);
    }
}
