package my.book.market.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import my.book.market.validation.Isbn;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class CreateBookRequestDto {
    @NonNull
    private String title;
    @NonNull
    private String author;
    @Isbn
    @NonNull
    private String isbn;
    @NonNull
    @Min(0)
    private BigDecimal price;
    @Size(max = 255)
    private String description;
    @Size(max = 255)
    private String coverImage;
    @NotNull
    private Set<Long> categoryIds = new HashSet<>();
}
