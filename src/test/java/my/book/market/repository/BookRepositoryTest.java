package my.book.market.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import my.book.market.model.Book;
import my.book.market.model.Category;
import my.book.market.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    private static final String SQL_SCRIPT_BEFORE_TEST
            = "classpath:database/test/add-books-with-category-to-books-and-categories-table.sql";
    private static final String SQL_SCRIPT_AFTER_TEST
            = "classpath:"
            + "database/test/delete-books-with-category-from-books-and-categories-table.sql";
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Verify find books by category id=1 when category ID exist")
    @Sql(scripts = SQL_SCRIPT_BEFORE_TEST, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = SQL_SCRIPT_AFTER_TEST, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_ValidCategoryId_ShouldReturnOneBookKobzar() {
        Book expected = createBookKobzar();
        List<Book> books = bookRepository.findAllByCategoryId(1L);
        assertThat(books).hasSize(1);
        assertThat(books.get(0)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Verify find books by category id=2 when category ID exist")
    @Sql(scripts = SQL_SCRIPT_BEFORE_TEST, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = SQL_SCRIPT_AFTER_TEST, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_ValidCategoryId_ShouldReturnOneBookLisovaPisnia() {
        Book expected = createBookLisovaPisnia();
        List<Book> books = bookRepository.findAllByCategoryId(2L);
        assertThat(books).hasSize(1);
        assertThat(books.get(0)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Verify find books by category when the category ID not existing")
    @Sql(scripts = SQL_SCRIPT_BEFORE_TEST, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = SQL_SCRIPT_AFTER_TEST, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_WithNonExistingCategoryId_ShouldReturnEmptyList() {
        Long nonExistingCategoryId = Long.MAX_VALUE;
        List<Book> books = bookRepository.findAllByCategoryId(nonExistingCategoryId);
        assertThat(books).hasSize(0);
    }

    private Book createBookKobzar() {
        Book book = new Book(1L);
        book.setTitle("Kobzar");
        book.setAuthor("Shevchenko T. G.");
        book.setIsbn("0123456789");
        book.setPrice(BigDecimal.valueOf(100.55));
        Category category = new Category();
        category.setId(1L);
        category.setName("Poetry");
        book.setCategories(Set.of(category));
        return book;
    }

    private Book createBookLisovaPisnia() {
        Book book = new Book(2L);
        book.setTitle("Lisova pisnia");
        book.setAuthor("Lesia Ykrainka");
        book.setIsbn("9874563210");
        book.setPrice(BigDecimal.valueOf(150.75));
        Category category = new Category();
        category.setId(2L);
        category.setName("Fantasy");
        book.setCategories(Set.of(category));
        return book;
    }
}
