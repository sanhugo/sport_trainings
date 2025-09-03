package ru.doc.sport_trainings.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.doc.sport_trainings.bot.BotStatus;
import ru.doc.sport_trainings.keyboards.KeyboardFacade;
import ru.doc.sport_trainings.service.UserStateService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageHandler {
    KeyboardFacade keyboard;
    UserStateService stateService;

    public MessageHandler(KeyboardFacade keyboard, UserStateService stateService) {
        this.keyboard = keyboard;
        this.stateService = stateService;
    }

    public BotApiMethod<?> execute(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(update.getMessage().getChatId());
        switch(update.getMessage().getText())
        {
            case "Новый вид упражнения":
                sendMessage.setText("Введите название упражнения");
                stateService.newState(update.getMessage().getChatId(), BotStatus.NEW_EXERCISE);
                break;
            case "График упражнения":
                sendMessage.setText("Выберите упражнение");
                stateService.newState(update.getMessage().getChatId(), BotStatus.GRAPH_WAITING);
                break;

            case "Добавить запись":

                sendMessage.setText("Выберите упражнение");
        }
        return sendMessage;
    }

}
