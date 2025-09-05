package ru.doc.sport_trainings.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.doc.sport_trainings.bot.BotStatus;
import ru.doc.sport_trainings.keyboards.KeyboardFacade;
import ru.doc.sport_trainings.service.UserService;
import ru.doc.sport_trainings.service.UserStateService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommandHandler {
    KeyboardFacade  keyboardFacade;
    UserService userService;
    UserStateService uss;

    public CommandHandler(KeyboardFacade keyboardFacade, UserService userService, UserStateService uss) {
        this.keyboardFacade = keyboardFacade;
        this.userService = userService;
        this.uss = uss;
    }

    public BotApiMethod<?> execute(Update update) {
        Message message = update.getMessage();
        if (message.getText().trim().equals("/start"))
        {
            SendMessage sendMessage= SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text("Добро пожаловать! Выберите пункт меню: ")
                    .build();
            uss.newState(update.getMessage().getChatId(), BotStatus.READY);
            System.out.println(uss.getState(update.getMessage().getChatId()));
            ReplyKeyboardMarkup rkm = keyboardFacade.menuKeyboard();
            if (userService.checkAdmin(message.getChatId()))
            {
                rkm.setKeyboard(keyboardFacade.addUserControlButton(rkm));
            }
            sendMessage.setReplyMarkup(rkm);
            return sendMessage;
        }
        return null;
    }
}
