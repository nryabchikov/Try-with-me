package by.ryabchikov.coursework.util;

import by.ryabchikov.coursework.dto.user.RegistrationUserDTO;
import by.ryabchikov.coursework.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class RegistrationUserValidator implements Validator {
    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return RegistrationUserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegistrationUserDTO user = (RegistrationUserDTO) target;

        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            errors.rejectValue("login", "", "This login is already exist.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "This email is already exist.");
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "",
                    "Password and confirm password don't match.");
        }
    }
}
