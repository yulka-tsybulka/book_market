package my.book.market.service.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import my.book.market.dto.order.CreateOrderRequestDto;
import my.book.market.dto.order.OrderDto;
import my.book.market.dto.order.OrderItemDto;
import my.book.market.dto.order.UpdateStatusRequestDto;
import my.book.market.exception.EntityNotFoundException;
import my.book.market.mapper.OrderItemMapper;
import my.book.market.mapper.OrderMapper;
import my.book.market.model.CartItem;
import my.book.market.model.Order;
import my.book.market.model.OrderItem;
import my.book.market.model.ShoppingCart;
import my.book.market.model.User;
import my.book.market.repository.order.OrderRepository;
import my.book.market.repository.orderitem.OrderItemRepository;
import my.book.market.repository.shoppingcart.ShoppingCartRepository;
import my.book.market.repository.user.UserRepository;
import my.book.market.service.OrderService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public OrderDto createOrder(Long userId, CreateOrderRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                                "Can't find user with id: " + userId));
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                "There is not found shopping cart for user with id " + userId));
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new EntityNotFoundException(
                    "Can't create order: shopping cart of user with id " + userId
                            + " is empty!"
            );
        }
        Order order = new Order(user, Order.Status.PENDING,
                LocalDateTime.now(), requestDto.getShippingAddress());
        order.setOrderItems(createSetOfOrderItems(order, shoppingCart.getCartItems()));
        order.setTotal(getTotalPriceOfOrder(order.getOrderItems()));
        shoppingCartRepository.deleteById(shoppingCart.getId());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getAll(Long userId) {
        return orderRepository.findByIdOfCurrentUser(userId).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(Long orderId,
                                      UpdateStatusRequestDto requestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException(
                        "There is not found order with id " + orderId));
        order.setStatus(requestDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemDto> getAllOrderItems(Long orderId) {
        Order order = getOrderById(orderId);
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getItemById(Long orderId, Long itemId) {
        Order order = getOrderById(orderId);
        OrderItem orderItem = orderItemRepository
                .findById(itemId).orElseThrow(
                        () -> new EntityNotFoundException(
                                "OrderItem with id " + itemId
                                + " not found in order with id " + orderId));
        if (!order.getOrderItems().contains(orderItem)) {
            throw new EntityNotFoundException("Can't find order item with id " + itemId
                    + " in order with id " + orderId);
        }
        return orderItemMapper.toDto(orderItem);
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find order by id: " + orderId)
                );
    }

    private Set<OrderItem> createSetOfOrderItems(Order order, Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> new OrderItem(
                                order,
                                cartItem.getBook(),
                                cartItem.getQuantity(),
                                cartItem.getBook()
                                        .getPrice()
                                        .multiply(new BigDecimal(cartItem.getQuantity()))
                        )
                )
                .collect(Collectors.toSet());
    }

    private BigDecimal getTotalPriceOfOrder(Set<OrderItem> orderItems) {
        return new BigDecimal(
                orderItems.stream()
                        .map(OrderItem::getPrice)
                        .mapToInt(BigDecimal::intValue)
                        .sum()
        );
    }
}
