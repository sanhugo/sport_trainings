package ru.doc.sport_trainings.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.doc.sport_trainings.bot.BotStatus;
import ru.doc.sport_trainings.model.UserState;
import ru.doc.sport_trainings.repository.UserStateRepository;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserStateService {
    UserStateRepository userStateRepository;
    RedisTemplate<String,Object> redisTemplate;

    public UserStateService(UserStateRepository userStateRepository, RedisTemplate<String,Object> redisTemplate) {
        this.userStateRepository = userStateRepository;
        this.redisTemplate = redisTemplate;
    }
    public void newState(Long id, BotStatus status)
    {
        UserState s = new UserState(id,status,0);
        userStateRepository.save(s);
    }
    public void changeUserStatus(Long id, BotStatus status)
    {
        String key = "Users"+id;
        redisTemplate.opsForHash().put(key,"status",status);
        redisTemplate.expire(key, Duration.ofMinutes(10));
    }
    public void setUserStatusAndExerciseId(Long id, BotStatus status,Long exerciseId)
    {
        String key = "Users"+id;
        Map<String,Object> update = new HashMap<>();
        update.put("status",status);
        update.put("exerciseId",exerciseId);
        redisTemplate.opsForHash().putAll(key,update);
        redisTemplate.expire(key, Duration.ofMinutes(10));
    }
    public void deleteUserStatus(Long id)
    {
        userStateRepository.deleteById(id);
    }
}
