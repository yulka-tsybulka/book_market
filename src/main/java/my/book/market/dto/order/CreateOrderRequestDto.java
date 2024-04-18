package my.book.market.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateOrderRequestDto {
    @Size(max = 255)
    @NotNull
    private String shippingAddress;
}
