package my.book.market.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import my.book.market.dto.category.CategoryDto;
import my.book.market.dto.category.CreateCategoryRequestDto;
import my.book.market.service.CategoryService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static final String SQL_SCRIPT_BEFORE_TEST
            = "database/test/add-books-with-category-to-books-and-categories-table.sql";
    private static final String SQL_SCRIPT_AFTER_TEST
            = "database/test/delete-books-with-category-from-books-and-categories-table.sql";

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CategoryService categoryService;

    @Autowired
    private CategoryController categoryController;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource,
                          @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(SQL_SCRIPT_BEFORE_TEST)
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
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(SQL_SCRIPT_AFTER_TEST)
            );
        }
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Verify createCategory() method works")
    @Sql(scripts = {"classpath:database/test/delete-test-category-from-category-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidRequestDto_Success() throws Exception {
        CreateCategoryRequestDto requestDto = createTestCreateCategoryRequestDto();
        CategoryDto expected = createCategoryDto();
        Mockito.when(categoryService.save(createTestCreateCategoryRequestDto()))
                .thenReturn(expected);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/categories")).andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify createCategory() method return bad request when name is blanc")
    void createBook_NotValidCreateCategoryRequestDto_BadRequest() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto()
                .setName(null)
                .setDescription(null);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Verify getAll() method works")
    void getAll_AllCategoriesInCatalog_Success() throws Exception {
        List<CategoryDto> expectedCategories = getListCategoryDto();
        Mockito.when(categoryService.findAll()).thenReturn(expectedCategories);
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), CategoryDto[].class);
        Assertions.assertEquals(2, actual.length);
        Assertions.assertEquals(expectedCategories, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Verify getCategoryById() method works")
    void getCategoryById_ReturnById_Success() throws Exception {
        Long id = 1L;
        CategoryDto expected = createCategoryDto();
        Mockito.when(categoryService.getById(id)).thenReturn(expected);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/categories/{id}", id)
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(actual, expected, "id");
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Verify getCategoryById() method returns not found for non-existing Category id")
    void getCategoryById_NonExistingId_NotFound() throws Exception {
        Long nonExistingId = Long.MAX_VALUE;
        when(categoryService.getById(nonExistingId)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/categories/{id}", nonExistingId))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @DisplayName("Verify updateCategory() method works")
    void updateCategory_ValidRequestDto_Success() throws Exception {
        Long id = 1L;
        CreateCategoryRequestDto requestDto = createCategoryRequestDto();
        requestDto.setDescription("Test category");
        CategoryDto expected = createCategoryDto();
        expected.setDescription("Test category");
        Mockito.when(categoryService.update(id, requestDto)).thenReturn(expected);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/categories/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @DisplayName("Verify updateCategory method "
            + "returns IllegalArgumentException for non-existing Category id")
    public void updateCategory_NonExistingId_IllegalArgumentException() {
        Long nonExistingId = Long.MAX_VALUE;
        CreateCategoryRequestDto requestDto = createCategoryRequestDto();
        when(categoryService.update(nonExistingId, requestDto))
                .thenThrow(new IllegalArgumentException(
                        "Category with id " + nonExistingId + " not found"));
        assertThrows(IllegalArgumentException.class,
                () -> categoryController.updateCategory(nonExistingId, requestDto));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @DisplayName("Verify deleteCategory() method works")
    void deleteCategory_ValidId_Success() throws Exception {
        long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        mockMvc.perform(MockMvcRequestBuilders.get("/categories/{id}", id))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private CreateCategoryRequestDto createTestCreateCategoryRequestDto() {
        return new CreateCategoryRequestDto()
                .setName("Test");
    }

    private CreateCategoryRequestDto createCategoryRequestDto() {
        return new CreateCategoryRequestDto()
                .setName("Poetry");
    }

    private CategoryDto createCategoryDto() {
        return new CategoryDto()
                .setName("Poetry");
    }

    private List<CategoryDto> getListCategoryDto() {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        categoryDtoList.add(new CategoryDto()
                .setName("Poetry"));
        categoryDtoList.add(new CategoryDto()
                .setName("Fantasy"));
        return categoryDtoList;
    }
}
