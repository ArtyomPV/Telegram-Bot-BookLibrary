package ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.Telegram_Bot_BookLibrary.factory.AnswerMethodFactory;
import ru.prusov.Telegram_Bot_BookLibrary.factory.KeyboardFactory;
import ru.prusov.Telegram_Bot_BookLibrary.model.Book;
import ru.prusov.Telegram_Bot_BookLibrary.usecase.services.book.BookService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks.CallbackData.*;

@Component
@RequiredArgsConstructor
public class ShowBooksPaginationCallbackData implements CallbackCommand {

    private final TelegramClient client;
    private final BookService bookService;
    private int PAGE_SIZE = 2;

    @Override
    public String command() {
        return SHOW_BOOKS_PAGINATION;
    }

    @Override
    public void execute(CallbackQuery callbackQuery) {

        AnswerCallbackQuery answerCallbackQuery = AnswerMethodFactory.getAnswerCallbackQuery(callbackQuery.getId(),
                " Загружаю...");
        try {
            client.execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        int page = parseSecondInt(callbackQuery.getData(), 1);

        if (page < 1) page = 1;

        Page<Book> pageObj = bookService.findPage(page, PAGE_SIZE);
        int totalPages = pageObj.getTotalPages();
        if (totalPages == 0) totalPages = 1;
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Страница %d из %d%n%n", page, totalPages));
        for (Book b : pageObj.getContent()) {
            sb.append(String.format("%d) %s%n", b.getId(), b.getTitle()));
        }

        List<InlineKeyboardRow> rows = new ArrayList<>();
        InlineKeyboardRow row = new InlineKeyboardRow();

        // Кнопки для каждой книги
        for (Book b : pageObj.getContent()) {
            InlineKeyboardButton btn = new InlineKeyboardButton(b.getTitle());
            btn.setCallbackData(SHOW_BOOKS_PAGINATION + ":" + b.getId());
            row.add(btn);
        }
        rows.add(row);

        // Навигация
        InlineKeyboardRow nav = new InlineKeyboardRow();
//        List<InlineKeyboardButton> nav = new ArrayList<>();
        if (page > 1) {
            InlineKeyboardButton prev = new InlineKeyboardButton("⬅️ Prev");
            prev.setCallbackData(SHOW_BOOKS_PAGINATION + ":" + (page - 1));
            nav.add(prev);
        }
        if (page < totalPages) {
            InlineKeyboardButton next = new InlineKeyboardButton("Next ➡️");
            next.setCallbackData(SHOW_BOOKS_PAGINATION + ":" + (page + 1));
            nav.add(next);
        }
        if (!nav.isEmpty()) rows.add(nav);

        // Назад в меню
        InlineKeyboardButton back = new InlineKeyboardButton("◀️ Назад");
        back.setCallbackData(START);
        rows.add(new InlineKeyboardRow(back));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(rows);
        markup.setKeyboard(rows);

        EditMessageText editMessageText = AnswerMethodFactory.getEditMessageText(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                sb.toString(),
                markup
        );
        try {
            client.execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private int parseSecondInt(String data, int i) {
        String[] parts = data.split(":");
        return Integer.parseInt(parts[1]);
    }
}
