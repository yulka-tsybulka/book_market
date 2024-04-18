package my.book.market.service;

import java.util.List;
import my.book.market.dto.order.CreateOrderRequestDto;
import my.book.market.dto.order.OrderDto;
import my.book.market.dto.order.OrderItemDto;
import my.book.market.dto.order.UpdateStatusRequestDto;

public interface OrderService {
    OrderDto createOrder(Long userId, CreateOrderRequestDto requestDto);

    List<OrderDto> getAll(Long userId);

    OrderDto updateOrderStatus(Long orderId, UpdateStatusRequestDto statusRequestDto);

    List<OrderItemDto> getAllOrderItems(Long orderId);

    OrderItemDto getItemById(Long orderId, Long itemId);
}

