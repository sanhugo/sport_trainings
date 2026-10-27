package ru.doc.sport_trainings.handlers;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@NoArgsConstructor
public class HandlerFailure {
    public BotApiMethod<?> noUserInBase(Update update)
    {
        return SendMessage.builder()
                .chatId(chatId(update))
                .text("Вас нет в системе. Обратитесь к админу.")
                .build();
    }

    public BotApiMethod<?> blockedUserInBase(Update update)
    {
        return SendMessage.builder()
                .chatId(chatId(update))
                .text("Вы заблокированы в системе. Обратитесь к админу.")
                .build();
    }

    // у callback-запроса нет update.getMessage(), чат берём из самого callback'а
    private long chatId(Update update)
    {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        }
        return update.getMessage().getChatId();
    }
}
