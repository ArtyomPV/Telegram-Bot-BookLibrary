package ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.Telegram_Bot_BookLibrary.factory.AnswerMethodFactory;
import ru.prusov.Telegram_Bot_BookLibrary.factory.KeyboardFactory;
import ru.prusov.Telegram_Bot_BookLibrary.model.Book;
import ru.prusov.Telegram_Bot_BookLibrary.usecase.services.book.BookService;

import java.util.ArrayList;
import java.util.List;

import static ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks.CallbackData.SHOW_BOOKS_PAGINATION;
import static ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks.CallbackData.START;

@Component
@RequiredArgsConstructor
public class ShowBooksPaginationCallbackData implements CallbackCommand {

    private final TelegramClient client;
    private final BookService bookService;
//    @Value("${page:size}")
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

        int page = 1;
        String[] parts = callbackQuery.getData().split(":");
        page = Integer.parseInt(parts[1]);
        if (page < 1) page = 1;

        sendBookPage(callbackQuery.getMessage().getChatId(), page);
    }

    private void sendBookPage(Long chatId, int page) {
        List<Book> pageBooks = bookService.findPage(page, PAGE_SIZE);
        List<String> textButton = new ArrayList<>();
        List<String> dataButton = new ArrayList<>();
        List<Integer> configuration = new ArrayList<>();

        int total = bookService.totalCount();
        int totalPages = (int) Math.ceil(total / (double) PAGE_SIZE);

        if (pageBooks.isEmpty()) {
            SendMessage sendMessage = AnswerMethodFactory.getSendMessage(chatId,
                    "Страница пуста.");
            try {
                client.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Страница %d из %d%n%n", page, totalPages));
        for (Book b : pageBooks) {
            sb.append(String.format("%d) %s%n", b.getId(), b.getTitle()));
            textButton.add(b.getTitle());
            dataButton.add(SHOW_BOOKS_PAGINATION + ":" + b.getId());
            configuration.add(1);
        }

        textButton.add("⬅️ Prev");
        textButton.add("Next ➡️");
        dataButton.add(SHOW_BOOKS_PAGINATION + ":" + (page - 1));
        dataButton.add(SHOW_BOOKS_PAGINATION + ":" + (page + 1));
        configuration.add(2);

        textButton.add("◀️ Назад");
        dataButton.add(START);
        configuration.add(1);


        SendMessage sendMessage = AnswerMethodFactory.getSendMessage(chatId,
                sb.toString(),
                KeyboardFactory.getInlineKeyboard(
                        textButton,
                        configuration,
                        dataButton
                ));

        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
