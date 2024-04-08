package my.book.market.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.book.market.dto.shoppingcart.AddToCartRequestDto;
import my.book.market.dto.shoppingcart.CartItemDto;
import my.book.market.dto.shoppingcart.ShoppingCartDto;
import my.book.market.dto.shoppingcart.UpdateQuantityBookRequestDto;
import my.book.market.model.User;
import my.book.market.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management",
        description = "Endpoints for managing shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    @Operation(summary = "Add a cart item to the shopping cart",
            description = "Add a cart item to the shopping cart")
    @ResponseStatus(HttpStatus.CREATED)
    public CartItemDto addToCart(Authentication authentication,
                                 @RequestBody @Valid AddToCartRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addCartItemToCart(requestDto, user.getId());
    }

    @GetMapping
    @Operation(summary = "Get a user's shopping cart",
            description = "Get a user's shopping cart")
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getCartByUserId(user.getId());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/cart-items/{id}")
    @Operation(summary = "Update book's quantity by cart item id",
            description = "Update book's quantity by cart item id")
    public CartItemDto updateQuantityByCartItemId(
            @PathVariable("id") Long cartItemId,
            @RequestBody @Valid UpdateQuantityBookRequestDto requestDto
    ) {
        return shoppingCartService.updateQuantityOfCartItem(cartItemId, requestDto.getQuantity());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/cart-items/{id}")
    @Operation(summary = "Delete a book from the shopping cart by cart item id",
            description = "Delete a book from the shopping cart by cart item id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long cartItemId) {
        shoppingCartService.deleteCartItem(cartItemId);
    }
}
