package ru.doc.sport_trainings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.doc.sport_trainings.model.ExerciseType;

import java.util.Optional;

public interface ExerciseTypeRepository extends JpaRepository<ExerciseType,Long> {
}
