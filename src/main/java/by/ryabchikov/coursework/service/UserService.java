package by.ryabchikov.coursework.service;

import by.ryabchikov.coursework.config.MyUserDetails;
import by.ryabchikov.coursework.dto.user.UserProfileDTO;
import by.ryabchikov.coursework.mapper.UserMapper;
import by.ryabchikov.coursework.dto.user.RegistrationUserDTO;
import by.ryabchikov.coursework.model.user.User;
import by.ryabchikov.coursework.repository.UserRepository;
import by.ryabchikov.coursework.util.IAuthenticationFacade;
import by.ryabchikov.coursework.util.ImageGenerator;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final IAuthenticationFacade authenticationFacade;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Transactional
    public void registerUser(RegistrationUserDTO registrationUserDTO, String url) {
        if (userRepository.findByLogin(registrationUserDTO.getLogin()).isPresent()) {
            System.out.println("Login");
            throw new RuntimeException();
        }

        if (userRepository.findByEmail(registrationUserDTO.getEmail()).isPresent()) {
            System.out.println("Email");
            throw new RuntimeException();
        }

        User user = userMapper.toUser(registrationUserDTO);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnable(false);
        user.setVerificationCode(UUID.randomUUID().toString());
        user.setResetToken(UUID.randomUUID().toString());

        String initials = ImageGenerator.getInitials(user);
        try {
            // Specify the path where avatars will be saved
            String avatarPath = ImageGenerator.generateAvatar(initials, Paths.get("src/main/resources/static/uploads").toString(), user.getLogin());
            // Save the relative path to the database
            user.setPhotoFileName(Paths.get(avatarPath).getFileName().toString());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate avatar image");
        }

        userRepository.save(user);
        sendEmail(user, url);
    }
    @Transactional
    public void editUserProfile(UserProfileDTO userProfileDTO) {
        User user = getCurrentUser();

        user.setName(userProfileDTO.getName());
        user.setSurname(userProfileDTO.getSurname());
        user.setPatronymic(userProfileDTO.getPatronymic());
        user.setDateOfBirthday(userProfileDTO.getDateOfBirthday());
        user.setAddress(userProfileDTO.getAddress());

        userRepository.save(user);
    }
    public User getCurrentUser() {
        Authentication authentication = authenticationFacade.getAuthentication();
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        return myUserDetails.getUser();
    }

    public List<User> findUser(String query) {
        Optional<List<User>> list = userRepository.findByNameIsContainingIgnoreCaseOrLoginIsContainingIgnoreCaseOrSurnameIsContainingIgnoreCase(query, query, query);
        return list.orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }

    public void sendEmail(User user, String url) {

        String from = "nryabchikov24@gmail.com";
        String to = user.getEmail();
        String subject = "Account Verfication";
        String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "Nekitos4";

        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(from, "Nekitos4");
            helper.setTo(to);
            helper.setSubject(subject);

            content = content.replace("[[name]]", user.getLogin());
            String siteUrl = url + "/verify?code=" + user.getVerificationCode();

            System.out.println(siteUrl);

            content = content.replace("[[URL]]", siteUrl);

            helper.setText(content, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendEmail1(User user, String url) {

        String from = "nryabchikov24@gmail.com";
        String to = user.getEmail();
        String subject = "Reset Password";
        String content = "Dear [[name]],<br>" + "Please click the link below to reset your password:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">RESET PASSWORD</a></h3>" + "Thank you,<br>" + "Nekitos4";

        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(from, "Nekitos4");
            helper.setTo(to);
            helper.setSubject(subject);

            content = content.replace("[[name]]", user.getLogin());
            String siteUrl = url + "/resetPassword?token=" + user.getResetToken();

            System.out.println(siteUrl);

            content = content.replace("[[URL]]", siteUrl);

            helper.setText(content, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean verifyAccount(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
        if (user == null) {
            return false;
        } else {
            user.setEnable(true);
            user.setVerificationCode(null);
            userRepository.save(user);
            return true;
        }
    }
}
