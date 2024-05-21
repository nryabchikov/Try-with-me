package by.ryabchikov.coursework.controller;

import by.ryabchikov.coursework.dto.user.RegistrationUserDTO;
import by.ryabchikov.coursework.dto.user.ResetUserPasswordDTO;
import by.ryabchikov.coursework.model.user.User;
import by.ryabchikov.coursework.repository.UserRepository;
import by.ryabchikov.coursework.service.UserService;
import by.ryabchikov.coursework.util.ResetUserPasswordValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class LoginController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResetUserPasswordValidator validator;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/forgotPassword")
    public String getForgotPasswordPage(@ModelAttribute("user") RegistrationUserDTO registrationUserDTO) {
        return "forgot_password";
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(@ModelAttribute("user") @Validated User user,
                               BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
            return "forgot_password";
        }
        try {
            String url = request.getRequestURL().toString();
            url = url.replace(request.getServletPath(), "");
            Optional<User> user1 = userRepository.findByEmail(user.getEmail());
            if (user1.isPresent()) {
                User us = user1.get();
                userService.sendEmail1(us, url);
            }
            //userService.registerUser(registrationUserDTO, url);
            return "redirect:/login";
        } catch (Exception e) {
            return "forgot_password";
        }
    }

//    @GetMapping("/resetPassword")
//    public String getResetPasswordPage(@ModelAttribute("user") RegistrationUserDTO user, @RequestParam String token) {
//        System.out.println(1);
//        System.out.println(token);
//        return "reset_password";
//    }
//
//    @PostMapping("/resetPassword")
//    public String resetPassword(@RequestParam(name = "token") String token, Model model,
//                                @ModelAttribute("user") RegistrationUserDTO registrationUserDTO) {
//        User user = userRepository.findByResetToken(token);
//        System.out.println("hello");
//        System.out.println(token);
//        System.out.println(user);
//        if (user == null) {
//            model.addAttribute("msg", "User not found or invalid reset token.");
//        } else {
//            System.out.println(user);
//            System.out.println(registrationUserDTO);
//            user.setPassword(registrationUserDTO.getPassword());
//            userRepository.save(user);
//        }
//
////        Optional<User> user = userRepository.findByEmail(userEmail);
////        if (user.isPresent()) {
////
////        }
//
//        return "login";
//    }

    @GetMapping("/resetPassword")
    public String getResetPasswordPage(@RequestParam String token, Model model) {
        User user = userRepository.findByResetToken(token);
        if (user == null) {
            model.addAttribute("msg", "Invalid reset token.");
            return "error";
        }
        RegistrationUserDTO userDTO = new RegistrationUserDTO();
        userDTO.setEmail(user.getEmail());
        model.addAttribute("user", userDTO);
        model.addAttribute("token", token);
        return "reset_password";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String token, @ModelAttribute("user") ResetUserPasswordDTO resetUserPasswordDTO,
                                Model model, BindingResult bindingResult) {
        validator.validate(resetUserPasswordDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("token", token);
            return "reset_password";
        }
        User user = userRepository.findByResetToken(token);
        if (user == null) {
            model.addAttribute("msg", "User not found or invalid reset token.");
            model.addAttribute("token", token);
            return "reset_password";
        } else {
            user.setPassword(passwordEncoder.encode(resetUserPasswordDTO.getPassword()));
            userRepository.save(user);
        }
        return "login";
    }

//    @PostMapping("/resetPassword")
//    public String resetPassword(@RequestParam String token, @ModelAttribute("user") RegistrationUserDTO registrationUserDTO,
//                                Model model, BindingResult bindingResult) {
//        validator.validate(registrationUserDTO, bindingResult);
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("bindingResult", bindingResult);
//            return "registration";
//        }
//        User user = userRepository.findByResetToken(token);
//        if (user == null) {
//            model.addAttribute("msg", "User not found or invalid reset token.");
//            return "reset_password";
//        } else {
//            user.setPassword(passwordEncoder.encode(registrationUserDTO.getPassword()));
//            userRepository.save(user);
//        }
//        return "login";
//    }

}
