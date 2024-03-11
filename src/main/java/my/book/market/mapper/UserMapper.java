package my.book.market.mapper;

import my.book.market.config.MapperConfig;
import my.book.market.dto.user.UserRegistrationRequestDto;
import my.book.market.dto.user.UserResponseDto;
import my.book.market.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
