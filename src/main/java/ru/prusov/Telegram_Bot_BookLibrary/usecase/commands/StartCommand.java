package ru.prusov.Telegram_Bot_BookLibrary.usecase.commands;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.Telegram_Bot_BookLibrary.factory.AnswerMethodFactory;
import ru.prusov.Telegram_Bot_BookLibrary.factory.KeyboardFactory;

import java.util.List;

import static ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks.CallbackData.*;


@Component
@AllArgsConstructor
public class StartCommand implements Command {

    private TelegramClient client;

    @Override
    public UserCommand command() {
        return UserCommand.START;
    }

    @Override
    public void execute(Message message) {
        String lastMessageText = message.getText();
        long chatId = message.getChatId();

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
