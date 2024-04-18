package my.book.market.repository.orderitem;

import java.util.List;
import java.util.Optional;
import my.book.market.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("FROM OrderItem oi "
            + "LEFT JOIN FETCH oi.order o "
            + "LEFT JOIN FETCH oi.book "
            + "WHERE o.id = :orderId ")
    List<OrderItem> findAllOrderItemByOrderId(Long orderId);

    @Query("FROM OrderItem oi "
            + "LEFT JOIN FETCH oi.order o "
            + "LEFT JOIN FETCH oi.book "
            + "WHERE oi.id = :id "
            + "AND o.id = :orderId ")
    Optional<OrderItem> findByIdAndOrderId(Long id, Long orderId);
}
