package ru.doc.sport_trainings.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.doc.sport_trainings.keyboards.KeyboardFacade;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageHandler {
    KeyboardFacade keyboard;

    public MessageHandler(KeyboardFacade keyboard) {
        this.keyboard = keyboard;
    }

    public BotApiMethod<?> execute(Update update) {
        SendMessage sm= SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Данная команда отсутствует. Выберите другую команду")
                .build();
        sm.setReplyMarkup(keyboard.menuKeyboard());
        return sm;
    }
}
