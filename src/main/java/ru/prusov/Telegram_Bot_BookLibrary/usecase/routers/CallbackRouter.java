package ru.prusov.Telegram_Bot_BookLibrary.usecase.routers;

import org.springframework.stereotype.Component;
import ru.prusov.Telegram_Bot_BookLibrary.usecase.callbacks.CallbackCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class CallbackRouter {

    private final HashMap<String, CallbackCommand> handlerMap = new HashMap<>();

    public CallbackRouter(List<CallbackCommand> handlers) {
        handlers.forEach(handler -> handlerMap.put(handler.command(), handler));
    }

    public Optional<CallbackCommand> getHandler(String callbackData) {
        return Optional.ofNullable(handlerMap.get(callbackData));
    }
}
