package ru.doc.sport_trainings.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.doc.sport_trainings.repository.UserStateRepository;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserStateService {
    UserStateRepository userStateRepository;

    public UserStateService(UserStateRepository userStateRepository) {
        this.userStateRepository = userStateRepository;
    }

}
