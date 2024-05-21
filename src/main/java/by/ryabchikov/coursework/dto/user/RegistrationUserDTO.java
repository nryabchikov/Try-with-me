package by.ryabchikov.coursework.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationUserDTO {

    @Size(min = 5, max = 30, message = "Login size should be between 5 and 30.")
    private String login;

    @Size(min = 8, max = 30, message = "Password size should be between 8 and 30.")
    private String password;

    @Size(min = 8, max = 30, message = "Password size should be between 8 and 30.")
    private String confirmPassword;

    @NotBlank(message = "Email shouldn't be null.")
    @Email
    private String email;
}
