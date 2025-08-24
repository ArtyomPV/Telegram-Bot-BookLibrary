package ru.prusov.Telegram_Bot_BookLibrary.usecase.services.book;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.prusov.Telegram_Bot_BookLibrary.model.Book;
import ru.prusov.Telegram_Bot_BookLibrary.model.repositroy.BookRepository;

import java.util.Optional;

/**
 * Page<Book> — это интерфейс из Spring Data (org.springframework.data.domain.Page). Он представляет собой результат постраничного (пагинационного) запроса к репозиторию: набор сущностей «страница» + метаданные о всей выборке (номер страницы, размер, общее количество элементов, общее число страниц и т.д.).
 *
 * Основные свойства Page
 * getContent() — список объектов текущей страницы (List).
 * getNumber() — номер текущей страницы (0‑based).
 * getSize() — размер страницы (количество элементов на странице).
 * getTotalElements() — общее количество элементов во всей выборке.
 * getTotalPages() — общее количество страниц.
 * hasNext(), hasPrevious(), isFirst(), isLast() — удобные булевы методы.
 * getPageable() — объект Pageable, который использовался для запроса.
 */
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Page<Book> findPage(int page, int pageSize) {
        if (page < 1) page = 1;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("id").ascending());
        return bookRepository.findAll(pageable);
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public long count() {
        return bookRepository.count();
    }

    @PostConstruct
    public void init() {
        if (bookRepository.count() == 0) {
            bookRepository.save(Book.builder()
                    .title("Мастер и Маргарита")
                    .author("М. Булгаков")
                    .year(1967).build());
            bookRepository.save(Book.builder()
                    .title("Преступление и наказание")
                    .author("Ф. Достоевский")
                    .year(1866).build());
            bookRepository.save(Book.builder()
                    .title("Анна Каренина")
                    .author("Л. Толстой")
                    .year(1877).build());
            bookRepository.save(Book.builder()
                    .title("Идиот")
                    .author("Ф. Достоевский")
                    .year(1869).build());
        }
    }
}
