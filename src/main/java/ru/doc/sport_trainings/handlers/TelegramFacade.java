package ru.doc.sport_trainings.handlers;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.doc.sport_trainings.bot.SportBot;
import ru.doc.sport_trainings.service.UserService;

import java.io.IOException;

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
    public BotApiMethod<?> handleUpdate(Update update, SportBot bot) throws TelegramApiException, IOException {
        System.out.println(update);
        long id;
        if (update.hasMessage() && update.getMessage().hasText()) {
            id = update.getMessage().getChatId();
        }
        else {
            id = update.getCallbackQuery().getFrom().getId();
        }
        boolean exists = userService.checkUser(id,true);
        if (exists) {
            boolean isNotBlocked = userService.checkUser(id,false);
            if (!isNotBlocked) {
            if (update.hasCallbackQuery()) {
                return callbackHandler.work(update,bot);
            } else {
                Message message = update.getMessage();
                System.out.println(message.getText());
                if (message.getText().startsWith("/")) {
                    System.out.println("CHECKED COMMAND!");
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


