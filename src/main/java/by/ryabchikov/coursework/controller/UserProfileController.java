package by.ryabchikov.coursework.controller;

import by.ryabchikov.coursework.dto.user.UserProfileDTO;
import by.ryabchikov.coursework.model.user.User;
import by.ryabchikov.coursework.repository.UserRepository;
import by.ryabchikov.coursework.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@Controller
@AllArgsConstructor
public class UserProfileController {
    private final UserService userService;
    private final UserRepository userRepository;

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
        return "profile";
    }














    //глянуть
    @PostMapping("/profile/uploadPhoto")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        // Проверить, что файл существует и не пустой
        if (file.isEmpty()) {
            // Обработать ошибку, если файл пустой
        }
        User user = userService.getCurrentUser();
        try {
//            // Сохранить файл на сервере, например, в папке uploads
//            byte[] bytes = file.getBytes();
//            Path path = Paths.get("uploads/" + file.getOriginalFilename());
//            user.setPhotoFileName("/Users/nikitaryabchikov/Desktop/" + file.getOriginalFilename());
//            userRepository.save(user);
//            Files.write(path, bytes);

            String uploadsDir = "/uploads/";
            String currentDir = System.getProperty("user.dir"); // Получаем текущую директорию проекта
            String absolutePath = currentDir + "/src/main/resources/static" + uploadsDir;

            // Создать директорию, если её еще нет
            Files.createDirectories(Paths.get(absolutePath));

            // Сохранить файл на сервере в папке uploads
            byte[] bytes = file.getBytes();
            String filename = StringUtils.cleanPath(file.getOriginalFilename()); // Получаем имя файла без спецсимволов
            Path path = Paths.get(absolutePath + filename);
            user.setPhotoFileName(filename); // Сохраняем путь к файлу в объект пользователя
            userRepository.save(user); // Сохраняем пользователя в базе данных
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/profile";
    }



}
