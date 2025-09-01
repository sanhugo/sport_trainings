package ru.doc.sport_trainings.bot;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebhookController {

    private final SportBot telegramBot;

    public WebhookController(SportBot telegramBot) {
        this.telegramBot = telegramBot;
    }

// point for message
    @PostMapping("/callback/webhook")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBot.onWebhookUpdateReceived(update);
    }
}
