package by.ryabchikov.coursework.util;

import by.ryabchikov.coursework.dto.user.ResetUserPasswordDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class ResetUserPasswordValidator implements Validator {
    private final int MIN_LENGTH = 8;
    @Override
    public boolean supports(Class<?> clazz) {
        return ResetUserPasswordDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ResetUserPasswordDTO user = (ResetUserPasswordDTO) target;

        if (user.getPassword().length() < MIN_LENGTH) {
            errors.rejectValue("password", "",
                    "Password size should be between 8 and 30.");
        }

        if (user.getConfirmPassword().length() < MIN_LENGTH) {
            errors.rejectValue("confirmPassword", "",
                    "Password size should be between 8 and 30.");
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "",
                    "Password and confirm password don't match.");
        }
    }
}

