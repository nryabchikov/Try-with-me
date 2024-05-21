package by.ryabchikov.coursework.controller;

import by.ryabchikov.coursework.dto.user.RegistrationUserDTO;

import by.ryabchikov.coursework.service.UserService;
import by.ryabchikov.coursework.util.RegistrationUserValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class RegistrationController {
    private final UserService userService;
    private final RegistrationUserValidator validator;

    @GetMapping("/register")
    public String getRegistrationPage(@ModelAttribute("user") RegistrationUserDTO registrationUserDTO) {
        return "registration";
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Validated RegistrationUserDTO registrationUserDTO,
                               BindingResult bindingResult, Model model, HttpServletRequest request) {
        validator.validate(registrationUserDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
            return "registration";
        }
        try {
            String url = request.getRequestURL().toString();
            url = url.replace(request.getServletPath(), "");
            userService.registerUser(registrationUserDTO, url);
            return "redirect:/login";
        } catch (Exception e) {
            return "registration";
        }
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam String code, Model model) {
        boolean isVerified = userService.verifyAccount(code);
        if (isVerified) {
            model.addAttribute("msg", "Your account is verified successfully!");
        } else {
            model.addAttribute("msg", "Your verification code is incorrect or already verified.");
        }
        return "message";
    }
}
