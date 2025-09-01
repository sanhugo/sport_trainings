package ru.doc.sport_trainings.bot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import ru.doc.sport_trainings.handlers.TelegramFacade;

@Configuration
public class BotConfig {
    private final TelegramBotConfig botConfig;

    public BotConfig(TelegramBotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(botConfig.getWebHookPath()).build();
    }

    @Bean
    public SportBot springWebhookBot(SetWebhook setWebhook, TelegramFacade telegramFacade) {
        SportBot bot = new SportBot(telegramFacade, setWebhook,botConfig.getBotToken());
        bot.setBotToken(botConfig.getBotToken());
        bot.setBotUsername(botConfig.getUserName());
        bot.setBotPath(botConfig.getWebHookPath());
        return bot;
    }
}
