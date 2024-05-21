package by.ryabchikov.coursework.repository;

import by.ryabchikov.coursework.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    //возможно нужно будет optional
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    Optional<List<User>> findByNameOrLoginOrSurname(String name, String login, String Surname);
    Optional<List<User>> findByNameIsContainingIgnoreCaseOrLoginIsContainingIgnoreCaseOrSurnameIsContainingIgnoreCase(String name, String login, String surname);
    User findByVerificationCode(String code);
    User findByResetToken(String token);
}
