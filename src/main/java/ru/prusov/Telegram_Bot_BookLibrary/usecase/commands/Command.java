package ru.prusov.Telegram_Bot_BookLibrary.usecase.commands;


import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface Command {

    UserCommand command();

    void execute(Message message);
}
