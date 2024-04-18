package my.book.market.mapper;

import my.book.market.config.MapperConfig;
import my.book.market.dto.order.OrderDto;
import my.book.market.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(source = "orderItems", target = "orderItems")
    OrderDto toDto(Order order);
}
