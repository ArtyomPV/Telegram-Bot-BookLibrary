package ru.prusov.Telegram_Bot_BookLibrary.factory;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@UtilityClass
public class AnswerMethodFactory {
    public static SendMessage getSendMessage(Long chatId, String text, ReplyKeyboard keyboard) {

        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .disableWebPagePreview(true)
                .replyMarkup(keyboard)
                .build();
    }

    public static SendMessage getSendMessage(Long chatId, String text) {
        return getSendMessage(chatId, text, null);
    }

    public static EditMessageText getEditMessageText(Long chatId, Integer messageId, String text, InlineKeyboardMarkup keyboardMarkup) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .replyMarkup(keyboardMarkup)
                .build();
    }

    public static EditMessageText getEditMessageText(Long chatId, Integer messageId, String text) {
        return getEditMessageText(chatId, messageId, text, null);
    }

    public static EditMessageText getEditMessageText(CallbackQuery callbackQuery, String text, InlineKeyboardMarkup keyboard) {
        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(text)
                .replyMarkup(keyboard)
                .build();
    }

    public static DeleteMessage getDeleteMessage(Long chatId, Integer messageId) {
        return DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();
    }

    public static AnswerCallbackQuery getAnswerCallbackQuery(String callbackQueryId, String text) {
        return AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQueryId)
                .text(text)
                .build();
    }
}
