package by.ryabchikov.coursework.model.user;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;
    private String password;
    private String email;

    /////////необязательные поля ввод уже в профиле

    private String name;
    private String surname;
    private String patronymic;
    @Column(name = "date_of_birthday")
    private LocalDate dateOfBirthday;
    private String address;

    @Column(name = "photo_filename")
    private String photoFileName;

    // поля для email verification
    private boolean enable;
    @Column(name = "verification_code")
    private String verificationCode;

    //поля для reset password
    @Column(name = "reset_token")
    private String resetToken;
}
