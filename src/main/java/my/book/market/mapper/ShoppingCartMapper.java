package my.book.market.mapper;

import my.book.market.config.MapperConfig;
import my.book.market.dto.shoppingcart.ShoppingCartDto;
import my.book.market.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "cartItems", target = "cartItems", qualifiedByName = "setOfCartItemsToDto")
    @Mapping(target = "userId", source = "user.id")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    ShoppingCart toModel(ShoppingCartDto shoppingCartDto);
}
