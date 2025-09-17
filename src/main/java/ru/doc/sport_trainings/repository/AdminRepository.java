package ru.doc.sport_trainings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.doc.sport_trainings.model.Admin;

public interface AdminRepository extends JpaRepository<Admin,Long> {
    boolean existsByTgid(Long tgid);
}
