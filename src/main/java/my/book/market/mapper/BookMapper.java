package my.book.market.mapper;

import java.util.stream.Collectors;
import my.book.market.config.MapperConfig;
import my.book.market.dto.book.BookDto;
import my.book.market.dto.book.BookDtoWithoutCategoryIds;
import my.book.market.dto.book.CreateBookRequestDto;
import my.book.market.model.Book;
import my.book.market.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoryIds(
                book.getCategories().stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet())
        );
    }

    @Mapping(target = "categories", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto requestDto) {
        book.setCategories(
                requestDto.getCategoryIds().stream()
                        .map(Category::new)
                        .collect(Collectors.toSet())
        );
    }

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);
}
