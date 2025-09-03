package ru.doc.sport_trainings.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.doc.sport_trainings.model.UserState;

@Repository
public interface UserStateRepository extends CrudRepository<UserState, Long> {

    @Override
    void deleteById(Long aLong);
}
