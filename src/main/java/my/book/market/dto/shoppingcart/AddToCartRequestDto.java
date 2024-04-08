package my.book.market.dto.shoppingcart;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AddToCartRequestDto {
    private Long bookId;
    @Positive
    private int quantity;
}
