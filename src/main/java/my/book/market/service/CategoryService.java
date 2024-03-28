package my.book.market.service;

import java.util.List;
import my.book.market.dto.category.CategoryDto;
import my.book.market.dto.category.CreateCategoryRequestDto;

public interface CategoryService {
    CategoryDto save(CreateCategoryRequestDto requestDto);

    CategoryDto getById(Long id);

    List<CategoryDto> findAll();

    CategoryDto update(Long id, CreateCategoryRequestDto requestDto);

    void deleteById(Long id);
}
