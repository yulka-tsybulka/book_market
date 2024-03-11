package my.book.market.service;

import my.book.market.dto.user.UserRegistrationRequestDto;
import my.book.market.dto.user.UserResponseDto;
import my.book.market.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;
}
