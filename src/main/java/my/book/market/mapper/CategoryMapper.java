package my.book.market.mapper;

import my.book.market.config.MapperConfig;
import my.book.market.dto.category.CategoryDto;
import my.book.market.dto.category.CreateCategoryRequestDto;
import my.book.market.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CreateCategoryRequestDto requestDto);
}
