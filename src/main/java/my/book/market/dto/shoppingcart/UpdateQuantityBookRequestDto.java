package my.book.market.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateQuantityBookRequestDto {
    @Min(1)
    private int quantity;
}
