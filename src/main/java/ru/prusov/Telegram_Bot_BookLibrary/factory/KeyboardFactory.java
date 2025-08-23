package ru.prusov.Telegram_Bot_BookLibrary.factory;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class KeyboardFactory {
    public static InlineKeyboardMarkup getInlineKeyboard(List<String> text,
                                                         List<Integer> configuration,
                                                         List<String> data) {
        List<InlineKeyboardRow> keyboardRows = new ArrayList<>();
        int index = 0;
        for (Integer buttonRow : configuration) {
            InlineKeyboardRow row = new InlineKeyboardRow();
            for (int i = 0; i < buttonRow; i++) {
                InlineKeyboardButton button = InlineKeyboardButton.builder()
                        .text(text.get(index))
                        .callbackData(data.get(index))
                        .build();
                index++;
                row.add(button);
            }
            keyboardRows.add(row);
        }
        return new InlineKeyboardMarkup(keyboardRows);
    }
}
