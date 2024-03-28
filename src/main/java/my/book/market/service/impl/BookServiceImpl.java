package my.book.market.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import my.book.market.dto.book.BookDto;
import my.book.market.dto.book.BookDtoWithoutCategoryIds;
import my.book.market.dto.book.BookSearchParametersDto;
import my.book.market.dto.book.CreateBookRequestDto;
import my.book.market.exception.EntityNotFoundException;
import my.book.market.mapper.BookMapper;
import my.book.market.model.Book;
import my.book.market.repository.book.BookRepository;
import my.book.market.repository.book.BookSpecificationBuilder;
import my.book.market.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find book by id " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto params) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long categoryId) {
        return bookRepository.findAllByCategoryId(categoryId)
                .stream().map(bookMapper::toDtoWithoutCategories)
                .toList();
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto requestDto) {
        bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find book by id " + id));
        Book updatedBook = bookMapper.toModel(requestDto);
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
        return bookMapper.toDto(updatedBook);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
