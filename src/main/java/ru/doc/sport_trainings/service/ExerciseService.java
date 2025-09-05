package ru.doc.sport_trainings.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.doc.sport_trainings.DTO.ExerciseTypeDTO;
import ru.doc.sport_trainings.model.ExerciseType;
import ru.doc.sport_trainings.repository.ExerciseTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExerciseService {
    ExerciseTypeRepository typeRepo;

    public ExerciseService(ExerciseTypeRepository typeRepo) {
        this.typeRepo = typeRepo;
    }
    public List<ExerciseTypeDTO> getExerciseTypes() {
        return typeRepo.findAllDTOs();
    }
    public String getExerciseType(long userID) {
        return typeRepo.findById(userID).get().getName();
    }
    public boolean addExerciseType(String exerciseType) {
        String exerciseTypeName = exerciseType.trim().toLowerCase();
        boolean et = typeRepo.existsByName(exerciseTypeName);
        if (et)
        {
            return false;
        }
        else {
            ExerciseType t =  new ExerciseType();
            t.setName(exerciseType);
            typeRepo.save(t);
            return true;
        }
    }
}
