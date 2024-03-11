package my.book.market.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegistrationRequestDto {
    @NotBlank
    @Email(message = "Please provide a valid email address")
    private String email;
    @NotBlank
    @Size(min = 4, max = 10, message = "Password must be at least 4 characters long")
    private String password;
    @NotBlank
    @Size(min = 4, max = 10, message = "Password must be at least 4 characters long")
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String shippingAddress;

    @AssertTrue(message = "Passwords do not match")
    private boolean isPasswordsMatch() {
        return password.equals(repeatPassword);
    }
}
