package ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackCommand {

    String command();

    void execute(CallbackQuery callbackQuery);
}
