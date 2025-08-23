package ru.prusov.Telegram_Bot_BookLibrary.usecase.services.book;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import ru.prusov.Telegram_Bot_BookLibrary.model.Book;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;

@Service
public class BookService {

    private final HashMap<Long, Book> books = new LinkedHashMap<>();

    @PostConstruct
    private void init() {
        books.put(1L, new Book(1L, "Мастер и Маргарита", "Михаил Булгаков", 1967));
        books.put(2L, new Book(2L, "Преступление и наказание", "Фёдор Достоевский", 1866));
        books.put(3L, new Book(3L, "Война и мир", "Лев Толстой", 1869));
    }

    public Collection<Book> findAll() {
        return books.values();
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(books.get(id));
    }
}
