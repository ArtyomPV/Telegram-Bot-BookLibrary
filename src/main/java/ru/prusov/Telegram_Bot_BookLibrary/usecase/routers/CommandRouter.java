package ru.prusov.Telegram_Bot_BookLibrary.usecase.routers;

import org.springframework.stereotype.Component;
import ru.prusov.Telegram_Bot_BookLibrary.usecase.commands.Command;
import ru.prusov.Telegram_Bot_BookLibrary.usecase.commands.UserCommand;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CommandRouter {

    private Map<UserCommand, Command> handlerMap = new EnumMap<>(UserCommand.class);

    public CommandRouter(List<Command> handlers) {
        handlers.forEach(handler -> {
            handlerMap.put(handler.command(), handler);
        });
    }

    public Optional<Command> getHandler(UserCommand userCommand) {
        return Optional.ofNullable(handlerMap.get(userCommand));
    }
}
