package my.book.market.dto.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import my.book.market.model.Order;

@Data
@Accessors(chain = true)
public class UpdateStatusRequestDto {
    @NonNull
    private Order.Status status;

    @JsonCreator
    public UpdateStatusRequestDto(@JsonProperty("status") String status) {
        this.status = Order.Status.valueOf(status);
    }
}
