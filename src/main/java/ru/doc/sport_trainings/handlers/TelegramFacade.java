package ru.doc.sport_trainings.handlers;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.doc.sport_trainings.bot.SportBot;
import ru.doc.sport_trainings.service.UserService;

import java.io.IOException;

@Slf4j
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
        log.info("event=update_received type={} update_id={}", updateType(update), update.getUpdateId());
        long id;
        if (update.hasMessage() && update.getMessage().hasText()) {
            id = update.getMessage().getChatId();
        }
        else if (update.hasCallbackQuery()) {
            id = update.getCallbackQuery().getFrom().getId();
        }
        else {
            return null;
        }
        boolean exists = userService.checkUser(id,true);
        if (exists) {
            boolean isNotBlocked = userService.checkUser(id,false);
            if (!isNotBlocked) {
            if (update.hasCallbackQuery()) {
                return callbackHandler.work(update,bot);
            } else {
                Message message = update.getMessage();
                log.debug("Message text: {}", message.getText());
                if (message.getText().startsWith("/")) {
                    log.info("CHECKED_COMMAND");
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

    private static String updateType(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().hasText() ? "message" : "message_other";
        }
        if (update.hasCallbackQuery()) return "callback_query";
        if (update.hasEditedMessage()) return "edited_message";
        if (update.hasMyChatMember()) return "my_chat_member";
        return "other";
    }
}


