package ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks;

import lombok.AllArgsConstructor;
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

import static ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks.CallbackData.SHOW_ALL_BOOKS;
import static ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks.CallbackData.START;

@Component
@AllArgsConstructor
public class ShowAllBookCallbackCommand implements CallbackCommand {

    private final TelegramClient client;
    private final BookService bookService;

    @Override
    public String command() {
        return SHOW_ALL_BOOKS;
    }

    @Override
    public void execute(CallbackQuery callbackQuery) {
        StringBuilder sb = new StringBuilder();
        for (Book book : bookService.findAll()) {
            sb.append(String.format("%d: %s - %s, %d %n",
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getYear()));
        }
        SendMessage sendMessage = AnswerMethodFactory.getSendMessage(callbackQuery.getMessage().getChatId(),
                sb.toString(),
                KeyboardFactory.getInlineKeyboard(
                        List.of("Назад"),
                        List.of(1),
                        List.of(START)
                ));
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
