package my.book.market.repository.book;

import lombok.RequiredArgsConstructor;
import my.book.market.dto.book.BookSearchParametersDto;
import my.book.market.model.Book;
import my.book.market.repository.SpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements
        SpecificationBuilder<Book, BookSearchParametersDto> {
    private static final String AUTHOR_SEARCH_PARAMETER = "author";
    private static final String TITLE_SEARCH_PARAMETER = "title";
    private final BookSpecificationProviderManager specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.where(null);

        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(AUTHOR_SEARCH_PARAMETER)
                    .getSpecification(searchParameters.authors()));
        }

        if (searchParameters.titles() != null && searchParameters.titles().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider(TITLE_SEARCH_PARAMETER)
                    .getSpecification(searchParameters.titles()));
        }

        return spec;
    }
}
