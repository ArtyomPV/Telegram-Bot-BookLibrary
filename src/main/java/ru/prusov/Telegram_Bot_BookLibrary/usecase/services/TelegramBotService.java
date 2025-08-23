package ru.prusov.Telegram_Bot_BookLibrary.usecase.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.Telegram_Bot_BookLibrary.factory.AnswerMethodFactory;
import ru.prusov.Telegram_Bot_BookLibrary.factory.KeyboardFactory;
import ru.prusov.Telegram_Bot_BookLibrary.usecase.commands.Command;
import ru.prusov.Telegram_Bot_BookLibrary.usecase.commands.UserCommand;
import ru.prusov.Telegram_Bot_BookLibrary.usecase.routers.CallbackRouter;
import ru.prusov.Telegram_Bot_BookLibrary.usecase.routers.CommandRouter;
import ru.prusov.Telegram_Bot_BookLibrary.usecase.services.book.BookService;

import java.util.List;

import static ru.prusov.Telegram_Bot_BookLibrary.usecase.commands.UserCommand.START;
import static ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks.CallbackData.*;

@Slf4j
@Service
@EnableAsync
@RequiredArgsConstructor
public class TelegramBotService implements LongPollingSingleThreadUpdateConsumer, SpringLongPollingBot {

    private final String botToken;
    private final BookService bookService;
    private final TelegramClient client;
    private final CallbackRouter callbackRouter;
    private final CommandRouter commandRouter;

    @Override
    public void consume(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                handleMessage(update.getMessage());
            } else if (update.hasCallbackQuery()) {
                handleCallbackQuery(update.getCallbackQuery(), update.getMessage());
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery, Message message) throws TelegramApiException {

        String[] data = callbackQuery.getData().split(":");
        Long chatId = callbackQuery.getMessage().getChatId();
        String callbackQueryId = callbackQuery.getId();

        AnswerCallbackQuery userCallback = AnswerMethodFactory.getAnswerCallbackQuery(callbackQueryId,
                "Обработано");
        client.execute(userCallback);
        log.info("method {}: data - {}", TelegramBotService.class.getSimpleName(), data[0]);
        callbackRouter.getHandler(data[0]).ifPresentOrElse(
                callbackCommand -> {
                    log.info(callbackCommand.command());
                    callbackCommand.execute(callbackQuery);
                },
                () -> unknowCommand(chatId));
    }

    private void unknowCommand(long chatId) {
        SendMessage sendMessage = AnswerMethodFactory.getSendMessage(
                chatId,
                "Неизвестная команда, начните с команды /start"
        );
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleMessage(Message message) throws TelegramApiException {
        String text = message.getText();
        if (text.startsWith("/")) {
            UserCommand command = UserCommand.getFromString(message.getText());
            log.info("Execute user command: {}", text);
            log.info("User command: {}", command.getCommand());
            commandRouter.getHandler(command).ifPresentOrElse(
                    handler -> {
                        handler.execute(message);
                    },
                    () -> unknowCommand(message.getChatId())
            );
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        log.info("Registered bot running state is: {}", botSession.isRunning());
    }
}
