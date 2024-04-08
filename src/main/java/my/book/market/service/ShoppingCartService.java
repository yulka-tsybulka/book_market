package my.book.market.service;

import my.book.market.dto.shoppingcart.AddToCartRequestDto;
import my.book.market.dto.shoppingcart.CartItemDto;
import my.book.market.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getCartByUserId(Long userId);

    CartItemDto addCartItemToCart(AddToCartRequestDto requestDto, Long userId);

    CartItemDto updateQuantityOfCartItem(Long cartItemId, int quantity);

    void deleteCartItem(Long cartItemId);
}
