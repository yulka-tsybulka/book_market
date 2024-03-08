package my.book.market.repository.book.spec;

import java.util.Arrays;
import my.book.market.model.Book;
import my.book.market.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String SPECIFICATION_KEY = "title";

    @Override
    public String getKey() {
        return SPECIFICATION_KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get("title").in(Arrays.stream(params).toArray());
    }
}
