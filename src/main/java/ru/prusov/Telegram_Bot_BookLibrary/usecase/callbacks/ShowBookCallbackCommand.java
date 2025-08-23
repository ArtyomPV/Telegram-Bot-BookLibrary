package ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.Telegram_Bot_BookLibrary.factory.AnswerMethodFactory;
import ru.prusov.Telegram_Bot_BookLibrary.factory.KeyboardFactory;
import ru.prusov.Telegram_Bot_BookLibrary.model.Book;
import ru.prusov.Telegram_Bot_BookLibrary.usecase.services.book.BookService;

import java.util.List;
import java.util.Optional;

import static ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks.CallbackData.SHOW_BOOK;
import static ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks.CallbackData.SHOW_BOOK_MENU;

@Slf4j
@Component
@AllArgsConstructor
public class ShowBookCallbackCommand implements CallbackCommand {

    private final TelegramClient client;
    private final BookService bookService;

    @Override
    public String command() {
        return SHOW_BOOK;
    }

    @Override
    public void execute(CallbackQuery callbackQuery) {
        String idBookStr = callbackQuery.getData().split(":")[1];
        log.info(idBookStr);
        long idBook = Long.parseLong(idBookStr);
        bookService.findById(idBook).ifPresentOrElse(
                book -> {
                    SendMessage sendMessage = AnswerMethodFactory.getSendMessage(
                            callbackQuery.getMessage().getChatId(),
                            String.format("Название: %s%nАвтор: %s%nГод выпуска: %d",
                                    book.getTitle(), book.getAuthor(), book.getYear()),
                            KeyboardFactory.getInlineKeyboard(
                                    List.of("Назад"),
                                    List.of(1),
                                    List.of(SHOW_BOOK_MENU)
                            ));
                    try {
                        client.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }, () -> {
                    SendMessage sendMessage = AnswerMethodFactory.getSendMessage(
                            callbackQuery.getMessage().getChatId(),
                            "Книга не найдена",
                            KeyboardFactory.getInlineKeyboard(
                                    List.of("Назад"),
                                    List.of(1),
                                    List.of(SHOW_BOOK_MENU)
                            ));
                    try {
                        client.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
