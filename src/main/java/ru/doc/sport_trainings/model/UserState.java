package ru.doc.sport_trainings.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import ru.doc.sport_trainings.bot.BotStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value="Users")
public class UserState {
    @Id
    private long id;
    private BotStatus status;
    private long exerciseId;
}
