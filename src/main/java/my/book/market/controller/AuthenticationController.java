package my.book.market.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.book.market.dto.user.UserLoginRequestDto;
import my.book.market.dto.user.UserLoginResponseDto;
import my.book.market.dto.user.UserRegistrationRequestDto;
import my.book.market.dto.user.UserResponseDto;
import my.book.market.exception.RegistrationException;
import my.book.market.security.AuthenticationService;
import my.book.market.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication management",
        description = "Endpoints for login and registration")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "User account login", description = "User account login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/registration")
    @Operation(summary = "User registration", description = "User registration")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }
}
