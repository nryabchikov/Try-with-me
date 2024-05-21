package by.ryabchikov.coursework.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class LoginUserDTO {

    @Size(min = 5, max = 30)
    private String login;

    @Size(min = 8, max = 30)
    private String password;
}
