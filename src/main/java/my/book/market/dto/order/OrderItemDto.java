package my.book.market.dto.order;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;
    private Long bookId;
    @Positive
    private int quantity;
}
