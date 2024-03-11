package my.book.market.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotBlank
        @Size(min = 8, max = 20)
        @Email(message = "Please provide a valid email address")
        String email,
        @NotBlank
        @Size(min = 4, max = 10)
        String password
) {
}
