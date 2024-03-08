package my.book.market.mapper;

import my.book.market.config.MapperConfig;
import my.book.market.dto.book.BookDto;
import my.book.market.dto.book.CreateBookRequestDto;
import my.book.market.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
