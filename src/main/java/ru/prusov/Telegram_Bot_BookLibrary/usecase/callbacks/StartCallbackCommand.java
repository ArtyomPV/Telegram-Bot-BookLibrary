package ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.Telegram_Bot_BookLibrary.factory.AnswerMethodFactory;
import ru.prusov.Telegram_Bot_BookLibrary.factory.KeyboardFactory;
import ru.prusov.Telegram_Bot_BookLibrary.usecase.commands.UserCommand;

import java.util.List;

import static ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks.CallbackData.*;
import static ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks.CallbackData.OTHER_ACTION;

@Component
@AllArgsConstructor
public class StartCallbackCommand implements CallbackCommand {
    private final TelegramClient client;

    @Override
    public String command() {
        return START;
    }

    @Override
    public void execute(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        SendMessage sendMessage = AnswerMethodFactory.getSendMessage(chatId,
                "Добро пожаловать! Выберите действие:",
                KeyboardFactory.getInlineKeyboard(
                        List.of("Вывести все книги", "Показать книгу", "Другая кнопка"),
                        List.of(1, 1, 1),
                        List.of(SHOW_ALL_BOOKS, SHOW_BOOK_MENU, OTHER_ACTION)
                ));
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}
