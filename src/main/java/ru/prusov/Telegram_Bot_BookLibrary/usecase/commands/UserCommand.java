package ru.prusov.Telegram_Bot_BookLibrary.usecase.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@AllArgsConstructor
public enum UserCommand {
    START("/start");

    String command;

    public static UserCommand getFromString(String command) {
        for (UserCommand userCommand : UserCommand.values()) {
            if (command.equals(userCommand.getCommand())) {
                return userCommand;
            }
        }
        return null;
    }
}
