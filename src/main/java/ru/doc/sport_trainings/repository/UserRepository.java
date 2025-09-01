package ru.doc.sport_trainings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import ru.doc.sport_trainings.model.User;

import java.util.Collection;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT u.isBlocked from User u where u.id=:id")
    boolean findByIdEquals(Long id);

    boolean existsById(Long id);

    @Query("select u.isAdmin from User u where u.id=:id")
    Boolean findBooleanByIdEquals(Long id);
}
