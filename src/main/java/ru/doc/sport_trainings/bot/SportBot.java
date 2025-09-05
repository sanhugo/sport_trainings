package ru.doc.sport_trainings.bot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import ru.doc.sport_trainings.handlers.TelegramFacade;

import java.io.IOException;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SportBot extends SpringWebhookBot {
    String botPath;
    String botUsername;
    String botToken;

    private TelegramFacade telegramFacade;

    public SportBot(TelegramFacade telegramFacade, SetWebhook setWebhook, String botToken) {
        super(setWebhook, botToken);
        this.telegramFacade = telegramFacade;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return telegramFacade.handleUpdate(update,this);
        } catch (TelegramApiException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
