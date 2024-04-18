package my.book.market.mapper;

import my.book.market.config.MapperConfig;
import my.book.market.dto.order.OrderItemDto;
import my.book.market.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Named("orderItemsToDto")
    OrderItemDto toDto(OrderItem orderItem);
}
