package my.book.market.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import my.book.market.dto.shoppingcart.AddToCartRequestDto;
import my.book.market.dto.shoppingcart.CartItemDto;
import my.book.market.dto.shoppingcart.ShoppingCartDto;
import my.book.market.exception.EntityNotFoundException;
import my.book.market.mapper.CartItemMapper;
import my.book.market.mapper.ShoppingCartMapper;
import my.book.market.model.Book;
import my.book.market.model.CartItem;
import my.book.market.model.ShoppingCart;
import my.book.market.model.User;
import my.book.market.repository.book.BookRepository;
import my.book.market.repository.cartitem.CartItemRepository;
import my.book.market.repository.shoppingcart.ShoppingCartRepository;
import my.book.market.repository.user.UserRepository;
import my.book.market.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getCartByUserId(Long userId) {
        return shoppingCartMapper.toDto(getCart(userId));
    }

    public CartItemDto addCartItemToCart(AddToCartRequestDto requestDto, Long userId) {
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find book by id: " + requestDto.getBookId())
                );
        ShoppingCart shoppingCart = getCart(userId);
        Optional<CartItem> byBookIdAndShoppingCartId = cartItemRepository
                .findCartItemByBookIdAndShoppingCartId(book.getId(), shoppingCart.getId());
        if (byBookIdAndShoppingCartId.isPresent()) {
            CartItem cartItem = byBookIdAndShoppingCartId.get();
            return updateQuantityOfCartItem(
                    cartItem.getId(), cartItem.getQuantity() + requestDto.getQuantity()
            );
        }
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        shoppingCart.getCartItems().add(savedCartItem);
        shoppingCartRepository.save(shoppingCart);
        return cartItemMapper.toDto(savedCartItem);
    }

    @Override
    public CartItemDto updateQuantityOfCartItem(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't find cart item by id: " + cartItemId
                        )
                );
        cartItem.setQuantity(quantity);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    private ShoppingCart getCart(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    User userFromDb = userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Can't find user by id=" + userId));
                    shoppingCart.setUser(userFromDb);
                    return shoppingCartRepository.save(shoppingCart);
                });
    }
}
