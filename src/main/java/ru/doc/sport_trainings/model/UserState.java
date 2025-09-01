package ru.doc.sport_trainings.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Setter
@Getter
@RedisHash
public class UserState {

}
