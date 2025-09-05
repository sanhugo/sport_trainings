package ru.doc.sport_trainings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.doc.sport_trainings.DTO.ExerciseTypeDTO;
import ru.doc.sport_trainings.model.ExerciseType;

import java.util.List;
import java.util.Optional;

public interface ExerciseTypeRepository extends JpaRepository<ExerciseType,Long> {
    @Query("select et.id, et.name from ExerciseType et")
    List<ExerciseTypeDTO> findAllDTOs();

    @Query("select et.name from ExerciseType et where et.id=:userID")
    String findNameById(long userID);

    boolean existsByName(String name);
}
