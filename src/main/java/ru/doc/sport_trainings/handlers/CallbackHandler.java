package ru.doc.sport_trainings.handlers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.doc.sport_trainings.keyboards.KeyboardFacade;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CallbackHandler {
    KeyboardFacade keyboardFacade;
    public CallbackHandler(KeyboardFacade keyboardFacade) {
        this.keyboardFacade = keyboardFacade;
    }

    public BotApiMethod<?> execute(Update update)
    {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(update.getMessage().getChatId());
        switch(update.getMessage().getText())
        {
            case "Новое упражнение":
                    sendMessage.setText("Введите название упражнения");
        }
        return sendMessage;
    }
}
