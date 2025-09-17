package ru.doc.sport_trainings.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.doc.sport_trainings.repository.AdminRepository;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AdminService {
    AdminRepository repo;

    public AdminService(AdminRepository repo) {
        this.repo = repo;
    }
    public boolean checkAdmin(long id)
    {
        return repo.existsByTgid(id);
    }
}
