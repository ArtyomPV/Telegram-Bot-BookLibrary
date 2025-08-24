package ru.prusov.Telegram_Bot_BookLibrary.model.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.prusov.Telegram_Bot_BookLibrary.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
