package ru.doc.sport_trainings.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.doc.sport_trainings.DTO.ExerciseNoteAdditionDTO;
import ru.doc.sport_trainings.repository.ExerciseNoteRepository;

import java.time.LocalDate;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExerciseNoteService {
    ExerciseNoteRepository  exerciseNoteRepository;
    public ExerciseNoteService(ExerciseNoteRepository exerciseNoteRepository) {
        this.exerciseNoteRepository = exerciseNoteRepository;
    }
    public boolean createExerciseNote(ExerciseNoteAdditionDTO exerciseNote){
        if (exerciseNoteRepository.existsByUser_IdEqualsAndType_IdEqualsAndDateEquals(exerciseNote.getUserId(),exerciseNote.getTypeId(), LocalDate.now()))
        {
            return false;
        }
        else
        {
            exerciseNoteRepository.add(exerciseNote);
            return true;
        }
    }
}
