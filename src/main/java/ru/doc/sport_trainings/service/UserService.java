package ru.doc.sport_trainings.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.doc.sport_trainings.repository.UserRepository;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public boolean checkUser(long id, boolean flag)
    {
        if (flag)
        {
            return userRepository.existsById(id);
        }
        else {
            return userRepository.findByIdEquals(id);
        }
    }
    public boolean checkAdmin(long id)
    {
        return userRepository.findBooleanByIdEquals(id);
    }
}
