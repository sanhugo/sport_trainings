package ru.doc.sport_trainings.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.doc.sport_trainings.DTO.ExerciseNoteAdditionDTO;
import ru.doc.sport_trainings.DTO.ExerciseNoteGraphDTO;
import ru.doc.sport_trainings.repository.ExerciseNoteRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExerciseNoteService {
    ExerciseNoteRepository  exerciseNoteRepository;
    RedisTemplate<String,Object> redisTemplate;
    public ExerciseNoteService(ExerciseNoteRepository exerciseNoteRepository, RedisTemplate<String, Object> redisTemplate) {
        this.exerciseNoteRepository = exerciseNoteRepository;
        this.redisTemplate = redisTemplate;
    }
    public boolean createExerciseNote(ExerciseNoteAdditionDTO exerciseNote){
        if (exerciseNoteRepository.existsByUser_IdAndType_IdAndDate(exerciseNote.getUserId(),exerciseNote.getTypeId(),LocalDate.now()))
        {
            return false;
        }
        else
        {
            exerciseNoteRepository.insertExerciseNoteFromDTO(exerciseNote,LocalDate.now());
            return true;
        }
    }

    public List<ExerciseNoteGraphDTO> getNotesByExerciseType(long id, long exerciseId) {
        return exerciseNoteRepository.allDTOByUserIDandExerciseID(id,exerciseId);
    }
    public ExerciseNoteAdditionDTO getExerciseNote(long id,double number) {
        long exerciseId =Long.valueOf(redisTemplate.opsForHash().get("Users:"+id,"exerciseId").toString());
        return new ExerciseNoteAdditionDTO(id,exerciseId,number);
    }
}
