package ru.prusov.Telegram_Bot_BookLibrary.usecase.services.book;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import ru.prusov.Telegram_Bot_BookLibrary.model.Book;

import java.util.*;

@Service
public class BookServiceInMemory {

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

    /**
     * Создаем вспомогательный список со значениями из мапы
     * по номеру страницы умноженному на количество позиций в странице получаем позицию от куда будем извлекать значения
     * определяем конечную точку на странице, к отправной позиции прибавляем кол-во позиций на странице или последний индекс массива
     *
     * @param page     номер страницы
     * @param pageSize количество позиций на страницу
     * @return возвращает список книг в пределах страницы
     */
    public List<Book> findPage(int page, int pageSize) {
        List<Book> list = new ArrayList<>(books.values());
        int from = (page - 1) * pageSize;
        if (from >= list.size()) {
            return Collections.emptyList();
        }
        int to = Math.min(from + pageSize, list.size());
        return list.subList(from, to);
    }

    public int totalCount() {
        return books.size();
    }
}

