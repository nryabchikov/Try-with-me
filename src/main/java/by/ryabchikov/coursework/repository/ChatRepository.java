package by.ryabchikov.coursework.repository;

import by.ryabchikov.coursework.model.user.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatMessage, Long> {
}
