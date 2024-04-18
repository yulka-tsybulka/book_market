package my.book.market.repository.order;

import java.util.Optional;
import my.book.market.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("FROM Order o "
            + "LEFT JOIN FETCH o.orderItems "
            + "WHERE o.user.id = :userId ")
    Optional<Order> findByIdOfCurrentUser(Long userId);
}
