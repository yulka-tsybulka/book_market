package my.book.market.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import my.book.market.dto.book.BookDto;
import my.book.market.dto.book.CreateBookRequestDto;
import my.book.market.exception.EntityNotFoundException;
import my.book.market.service.BookService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
class BookControllerTest {
    protected static MockMvc mockMvc;
    private static final String SQL_SCRIPT_BEFORE_TEST
            = "database/books/add-books-with-category-to-books-and-categories-table.sql";
    private static final String SQL_SCRIPT_AFTER_TEST
            = "database/books/delete-books-with-category-from-books-and-categories-table.sql";
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookService bookService;

    @Autowired
    private BookController bookController;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
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
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @DisplayName("Verify createBook() method works")
    @Sql(scripts = {"classpath:database/books/delete-test-book-from-book-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = createBookRequestDtoTest();
        BookDto expected = createBookDto();
        when(bookService.save(requestDto)).thenReturn(expected);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/books")).andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual,"id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify createBook() method return bad request when Isnbn is not valid")
    void createBook_NotValidIsbnCreateBookRequestDto_BadRequest() throws Exception {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto()
                .setTitle("Test Book")
                .setAuthor("Test author")
                .setIsbn("01")
                .setPrice(BigDecimal.valueOf(90.55))
                .setDescription("Test book")
                .setCoverImage("Test cover")
                .setCategoryIds(Set.of(1L));
        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Verify getAll() method works")
    void getAll_AllBooksInCatalog_Success() throws Exception {
        BookDto bookDtoKobzar = createBookDtoKobzar();
        BookDto bookDtoKLisovaPisnia = createBookDtoLisovaPisnia();
        Pageable pageable = PageRequest.of(0, 10);
        List<BookDto> expectedBooks = List.of(bookDtoKobzar, bookDtoKLisovaPisnia);
        when(bookService.findAll(pageable)).thenReturn(expectedBooks);
        MvcResult result = mockMvc.perform(
                            MockMvcRequestBuilders.get("/books")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("page", "0")
                                    .param("size", "10")
                    )
                    .andExpect(status().isOk())
                    .andReturn();
        BookDto[] actual = objectMapper.readValue(result.getResponse()
                    .getContentAsByteArray(), BookDto[].class);
        Assertions.assertEquals(2, actual.length);
        Assertions.assertEquals(expectedBooks, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Verify getById() method works")
    void getById_ReturnById_Success() throws Exception {
        BookDto expected = createBookDtoKobzar();
        Long id = 1L;
        when(bookService.findById(id)).thenReturn(expected);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/books/{id}", id)
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(actual, expected, "id");
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Verify getBookById() method returns not found for non-existing book id")
    void getBookById_NonExistingId_NotFound() throws Exception {
        Long nonExistingId = Long.MAX_VALUE;
        when(bookService.findById(nonExistingId)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}", nonExistingId))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @DisplayName("Verify updateBookById() method works")
    void updateBookById_ValidRequestDto_Success() throws Exception {
        Long id = 1L;
        CreateBookRequestDto requestDto = createBookRequestDtoKobzar();
        requestDto.setPrice(BigDecimal.valueOf(200.22));
        BookDto expected = createBookDtoKobzar();
        expected.setPrice(BigDecimal.valueOf(200.22));
        when(bookService.updateById(id, requestDto)).thenReturn(expected);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .put("/books/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @DisplayName("Verify updateById method "
            + "returns EntityNotFoundException for non-existing book id")
    public void updateById_NonExistingId_EntityNotFoundException() {
        Long nonExistingId = Long.MAX_VALUE;
        CreateBookRequestDto requestDto = createBookRequestDtoKobzar();
        when(bookService.updateById(nonExistingId, requestDto))
                .thenThrow(new EntityNotFoundException("Can't find book by id " + nonExistingId));
        assertThrows(EntityNotFoundException.class,
                () -> bookController.updateById(nonExistingId, requestDto));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @DisplayName("Verify delete() method works")
    void deleteBook_ValidId_Success() throws Exception {
        long id = 2L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/books/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private BookDto createBookDtoKobzar() {
        return new BookDto()
                .setId(1L)
                .setTitle("Kobzar")
                .setAuthor("Shevchenko T. G.")
                .setIsbn("0123456789")
                .setPrice(BigDecimal.valueOf(100.55))
                .setCategoryIds(Set.of(1L));
    }

    private BookDto createBookDtoLisovaPisnia() {
        return new BookDto()
                .setId(2L)
                .setTitle("Lisova pisnia")
                .setAuthor("Lesia Ykrainka")
                .setIsbn("9874563210")
                .setPrice(BigDecimal.valueOf(150.75))
                .setCategoryIds(Set.of(2L));
    }

    private CreateBookRequestDto createBookRequestDtoKobzar() {
        return new CreateBookRequestDto()
                .setTitle("Kobzar")
                .setAuthor("Shevchenko T. G.")
                .setPrice(BigDecimal.valueOf(100.55))
                .setIsbn("0123456789")
                .setCategoryIds(Set.of(1L));
    }

    private BookDto createBookDto() {
        return new BookDto()
                .setTitle("Test Book")
                .setAuthor("Test author")
                .setIsbn("0000000099")
                .setPrice(BigDecimal.valueOf(90.55))
                .setDescription("Test book")
                .setCoverImage("Test cover")
                .setCategoryIds(Set.of(1L));
    }

    private CreateBookRequestDto createBookRequestDtoTest() {
        return new CreateBookRequestDto()
                .setTitle("Test Book")
                .setAuthor("Test author")
                .setPrice(BigDecimal.valueOf(90.55))
                .setIsbn("0000000099")
                .setCoverImage("Test cover")
                .setDescription("Test book")
                .setCategoryIds(Set.of(2L));
    }
}
