package by.ryabchikov.coursework.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileDTO {

    @Size(min = 5, max = 30)
    private String name;

    @Size(min = 5, max = 30)
    private String surname;

    @Size(min = 5, max = 30)
    private String patronymic;

    @NotNull
    private LocalDate dateOfBirthday;

    @Size(min = 5, max = 150)
    private String address;
}
