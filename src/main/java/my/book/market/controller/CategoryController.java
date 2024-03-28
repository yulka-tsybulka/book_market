package my.book.market.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import my.book.market.dto.book.BookDtoWithoutCategoryIds;
import my.book.market.dto.category.CategoryDto;
import my.book.market.dto.category.CreateCategoryRequestDto;
import my.book.market.service.BookService;
import my.book.market.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category management",
        description = "Endpoints for managing category of books")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Add category", description = "Add category to db")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid CreateCategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @GetMapping
    @Operation(summary = "Update the category by", description = "Update the category by")
    public List<CategoryDto> getAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by id", description = "Get category by id")
    public CategoryDto getCategoryById(@PathVariable("id") Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update the category by id", description = "Update the category by id")
    @PutMapping("/{id}")
    public CategoryDto updateCategory(
            @PathVariable("id") Long id, @RequestBody @Valid CreateCategoryRequestDto requestDto
    ) {
        return categoryService.update(id, requestDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the category by id", description = "Delete the category by id")
    public void deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteById(id);
    }

    @GetMapping("/{id}/books")
    @Operation(summary = "Get books by category", description = "Get books by category")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id) {
        return bookService.findBooksByCategoryId(id);
    }
}
