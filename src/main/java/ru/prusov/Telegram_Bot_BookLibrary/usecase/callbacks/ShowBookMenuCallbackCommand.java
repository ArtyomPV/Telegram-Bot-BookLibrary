package ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.Telegram_Bot_BookLibrary.factory.AnswerMethodFactory;
import ru.prusov.Telegram_Bot_BookLibrary.factory.KeyboardFactory;
import ru.prusov.Telegram_Bot_BookLibrary.model.Book;
import ru.prusov.Telegram_Bot_BookLibrary.usecase.services.book.BookService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks.CallbackData.SHOW_BOOK;
import static ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks.CallbackData.SHOW_BOOK_MENU;

@Component
@AllArgsConstructor
public class ShowBookMenuCallbackCommand implements CallbackCommand {

    private final TelegramClient client;
    private final BookService bookService;

    @Override
    public String command() {
        return SHOW_BOOK_MENU;
    }

    @Override
    public void execute(CallbackQuery callbackQuery) {
        List<String> data = new ArrayList<>();
        List<String> text = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
        Page<Book> allBooks = bookService.findPage(1, Integer.MAX_VALUE);
        allBooks.getContent().forEach(book -> {
                    text.add(book.getTitle());
                    data.add(SHOW_BOOK + ":" + book.getId());
                    indexes.add(1);
                }
        );
        SendMessage sendMessage = AnswerMethodFactory.getSendMessage(callbackQuery.getMessage().getChatId(),
                "Выберите книгу:",
                KeyboardFactory.getInlineKeyboard(
                        text,
                        indexes,
                        data
                ));

        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
