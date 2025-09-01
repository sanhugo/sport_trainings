package ru.doc.sport_trainings.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.doc.sport_trainings.repository.ExerciseTypeRepository;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExerciseService {
    ExerciseTypeRepository typeRepo;

    public ExerciseService(ExerciseTypeRepository typeRepo) {
        this.typeRepo = typeRepo;
    }
//    public boolean addExercise(String exerciseName)
//    {
//
//    }
}
