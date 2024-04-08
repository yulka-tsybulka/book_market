package my.book.market.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import my.book.market.config.MapperConfig;
import my.book.market.dto.shoppingcart.AddToCartRequestDto;
import my.book.market.dto.shoppingcart.CartItemDto;
import my.book.market.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookById")
    CartItem toModel(AddToCartRequestDto requestDto);

    @Named("setOfCartItemsToDto")
    default Set<CartItemDto> setOfCartItemsToDto(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }
}

