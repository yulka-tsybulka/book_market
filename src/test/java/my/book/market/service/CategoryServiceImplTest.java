package my.book.market.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import my.book.market.dto.category.CategoryDto;
import my.book.market.dto.category.CreateCategoryRequestDto;
import my.book.market.exception.EntityNotFoundException;
import my.book.market.mapper.CategoryMapper;
import my.book.market.model.Category;
import my.book.market.repository.category.CategoryRepository;
import my.book.market.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    private Category category;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("test category");
        category.setDescription("test");
    }

    @AfterEach
    void tearDown() {
        category = null;
    }

    @Test
    @DisplayName("Verify save category when the category exists")
    public void save_ValidCreateCategoryRequestDto_ShouldReturnValidCategoryDto() {
        CreateCategoryRequestDto requestDto = createCategoryRequestDto();
        CategoryDto expected = createCategoryDto();
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.save(requestDto);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Verify find category by Id when the category exists")
    public void getById_ValidCategoriesId_ShouldReturnValidCategoryDto() {
        CategoryDto expected = createCategoryDto();
        when(categoryRepository.findById(category.getId()))
                .thenReturn(Optional.ofNullable(category));
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.getById(category.getId());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Verify Exception in findById() method when the category id not exists")
    public void getById_WithNonExistingCategoryId_ShouldThrowException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(category.getId()));
        String expected = "Can't find category by id " + category.getId();
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Verify findAll categories when the categories exists")
    public void findAll_ValidCategories_ShouldReturnAllCategoriesDto() {
        CategoryDto categoryDto = createCategoryDto();
        List<Category> categories = List.of(category);
        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        List<CategoryDto> actual = categoryService.findAll();
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).isEqualTo(categoryDto);
    }

    @Test
    @DisplayName("Verify update category when id is exists")
    public void update_ValidId_ShouldUpdateCategory() {
        CreateCategoryRequestDto requestDto = createCategoryRequestDto();
        CategoryDto expected = createCategoryDto();
        when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(category));
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.update(category.getId(),requestDto);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Verify update method throw Exception when category id non-exist")
    public void update_NotValidId_ShouldThrowException() {
        CreateCategoryRequestDto requestDto = createCategoryRequestDto();
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> categoryService.update(category.getId(), requestDto));
        String expected = "Category with id " + category.getId() + " not found";
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Verify delete category when the id exists")
    public void delete_ValidId_ShouldDeleteBook() {
        categoryService.deleteById(category.getId());
        verify(categoryRepository, times(1)).deleteById(category.getId());
    }

    private CategoryDto createCategoryDto() {
        return new CategoryDto()
                .setId(category.getId())
                .setName(category.getName())
                .setDescription(category.getDescription());
    }

    private CreateCategoryRequestDto createCategoryRequestDto() {
        return new CreateCategoryRequestDto()
                .setName(category.getName())
                .setDescription(category.getDescription());
    }
}
