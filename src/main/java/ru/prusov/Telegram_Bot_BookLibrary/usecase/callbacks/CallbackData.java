package ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CallbackData {
    public static final String START = "/start";
    public static final String SHOW_BOOK = "show_book";
    public static final String OTHER_ACTION = "other_action";
    public static final String SHOW_BOOK_MENU = "show_book_menu";
    public static final String SHOW_ALL_BOOKS = "show_all_books";
    public static final String SHOW_BOOKS_PAGINATION = "show_books_pagination";
}
