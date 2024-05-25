package by.ryabchikov.coursework.controller;

import by.ryabchikov.coursework.config.MyUserDetails;
import by.ryabchikov.coursework.dto.user.UserProfileDTO;
import by.ryabchikov.coursework.model.user.User;
import by.ryabchikov.coursework.repository.UserRepository;
import by.ryabchikov.coursework.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class UserProfileController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final SessionRegistry sessionRegistry;

    @GetMapping("/profile/edit")
    public String getProfileEditPage() {
        return "edit_profile";
    }

    @PostMapping("profile/edit")
    public String editProfileOfUser(@ModelAttribute UserProfileDTO userProfileDTO,
                                                    @ModelAttribute("dateOfBirth") LocalDate dateOfBirth) {
        userProfileDTO.setDateOfBirthday(dateOfBirth);
        try {
            userService.editUserProfile(userProfileDTO);
            return "redirect:/profile";
        } catch (Exception e) {
            return "profile";
        }
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("currentUser", user);

        List<Object> principals = sessionRegistry.getAllPrincipals();
        List<String> usersNamesList = new ArrayList<>();
        for (Object principal: principals) {
            if (principal instanceof MyUserDetails) {
                usersNamesList.add(((MyUserDetails) principal).getUser().getLogin());
            }
        }

        model.addAttribute("activeUsers", usersNamesList);

        return "profile";
    }














    //глянуть
    @PostMapping("/profile/uploadPhoto")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        User user = userService.getCurrentUser();
        try {
            String uploadsDir = "/static/uploads/";
            String currentDir = System.getProperty("user.dir");
            String absolutePath = currentDir + "/src/main/resources" + uploadsDir;
            Files.createDirectories(Paths.get(absolutePath));
            byte[] bytes = file.getBytes();
            String filename = StringUtils.cleanPath(file.getOriginalFilename()); // Получаем имя файла без спецсимволов
            Path path = Paths.get(absolutePath + filename);
            user.setPhotoFileName(filename);
            userRepository.save(user);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/profile";
    }



}
