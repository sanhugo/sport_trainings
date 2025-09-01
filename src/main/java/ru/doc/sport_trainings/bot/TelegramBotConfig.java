package ru.doc.sport_trainings.bot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class TelegramBotConfig {
    @Value("${telegram.url}")
    String webHookPath;
    @Value("${telegram.username}")
    String userName;
    @Value("${telegram.token}")
    String botToken;
}