package my.book.market.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import my.book.market.dto.order.CreateOrderRequestDto;
import my.book.market.dto.order.OrderDto;
import my.book.market.dto.order.OrderItemDto;
import my.book.market.dto.order.UpdateStatusRequestDto;
import my.book.market.model.User;
import my.book.market.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management",
        description = "Endpoints for user's order management")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Place user's order by shipping address",
            description = "Place user's order by shipping address")
    public OrderDto createOrder(Authentication authentication,
                                @RequestBody @Valid CreateOrderRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.createOrder(user.getId(), requestDto);
    }

    @GetMapping
    @Operation(summary = "Get user's order history",
            description = "Get user's order history")
    public List<OrderDto> getOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getAll(user.getId());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{orderId}")
    @Operation(summary = "Update order status",
            description = "Update order status by order id")
    OrderDto updateOrderStatus(@PathVariable("orderId") Long orderId,
                               @RequestBody @Valid UpdateStatusRequestDto statusRequestDto) {
        return orderService.updateOrderStatus(orderId, statusRequestDto);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get all user's order items",
            description = "Get all user's order items for specific order")
    public List<OrderItemDto> getAllOrderItems(@PathVariable("orderId") Long orderId) {
        return orderService.getAllOrderItems(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get specific order item from order",
            description = "Get specific order item from order")
    OrderItemDto getItemById(@PathVariable("orderId") Long orderId,
                             @PathVariable("itemId") Long itemId) {
        return orderService.getItemById(orderId, itemId);
    }
}
