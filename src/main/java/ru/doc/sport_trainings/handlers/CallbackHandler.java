package ru.doc.sport_trainings.handlers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.doc.sport_trainings.bot.SportBot;
import ru.doc.sport_trainings.keyboards.KeyboardFacade;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CallbackHandler {
    SportBot bot;
    KeyboardFacade keyboardFacade;
    public CallbackHandler(SportBot bot, KeyboardFacade keyboardFacade) {
        this.bot = bot;
        this.keyboardFacade = keyboardFacade;
    }

    public BotApiMethod<?> work(Update update)
    {
        int updateMessage = update.getCallbackQuery().getMessage().getMessageId();
        long exerciseID = Long.parseLong(update.getCallbackQuery().getData());
        DeleteMessage delete = new DeleteMessage();
        delete.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        delete.setMessageId(updateMessage);
        try{
            bot.execute(delete);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }

    }

    public void deleteMessage(Long chatId, Integer messageId)
}
