package ru.doc.sport_trainings.repository;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;
import ru.doc.sport_trainings.model.UserState;

@Repository
public interface UserStateRepository extends KeyValueRepository<UserState, Long> {

    @Override
    void deleteById(Long aLong);
}
