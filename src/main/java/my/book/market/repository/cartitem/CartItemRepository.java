package my.book.market.repository.cartitem;

import java.util.Optional;
import my.book.market.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findCartItemByBookIdAndShoppingCartId(Long bookId, Long shoppingCartId);
}
