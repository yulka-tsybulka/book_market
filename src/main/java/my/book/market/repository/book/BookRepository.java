package my.book.market.repository.book;

import java.util.List;
import my.book.market.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query("SELECT b FROM Book b JOIN Fetch b.categories c WHERE c.id = :categoryId")
    List<Book> findAllByCategoryId(Long categoryId);
}
