package ru.doc.sport_trainings.handlers;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.doc.sport_trainings.service.UserService;

@Setter
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TelegramFacade {
    UserService userService;
    MessageHandler messageHandler;
    CommandHandler commandHandler;
    CallbackHandler callbackHandler;
    HandlerFailure handlerFailure;
    public TelegramFacade(MessageHandler messageHandler, CommandHandler commandHandler, CallbackHandler callbackHandler, UserService userService, HandlerFailure handlerFailure) {
        this.messageHandler = messageHandler;
        this.commandHandler = commandHandler;
        this.callbackHandler = callbackHandler;
        this.userService = userService;
        this.handlerFailure = handlerFailure;
    }
    public BotApiMethod<?> handleUpdate(Update update) {
        long id = update.getMessage().getChatId();
        boolean exists = userService.checkUser(id,true);
        if (exists) {
            boolean isNotBlocked = userService.checkUser(id,false);
            if (!isNotBlocked) {
            if (update.hasCallbackQuery()) {
                return callbackHandler.work(update);
            } else {
                Message message = update.getMessage();
                if (message.getText().startsWith("/")) {
                    return commandHandler.execute(update);
                } else {
                    return messageHandler.execute(update);
                }
            }
            }
            else  {
                return handlerFailure.blockedUserInBase(update);
            }
        }
        else
        {
            return handlerFailure.noUserInBase(update);
        }
    }
}


