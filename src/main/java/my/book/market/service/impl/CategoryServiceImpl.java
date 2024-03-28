package my.book.market.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import my.book.market.dto.category.CategoryDto;
import my.book.market.dto.category.CreateCategoryRequestDto;
import my.book.market.exception.EntityNotFoundException;
import my.book.market.mapper.CategoryMapper;
import my.book.market.model.Category;
import my.book.market.repository.category.CategoryRepository;
import my.book.market.service.CategoryService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto save(CreateCategoryRequestDto requestDto) {
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toModel(requestDto)));
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find category by id " + id)
                ));
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto requestDto) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Category with id " + id + " not found"));
        Category category = categoryMapper.toModel(requestDto);
        category.setId(existingCategory.getId());
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
