package ru.doc.sport_trainings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.doc.sport_trainings.DTO.ExerciseNoteAdditionDTO;
import ru.doc.sport_trainings.model.ExerciseNote;

import java.time.LocalDate;

public interface ExerciseNoteRepository extends JpaRepository<ExerciseNote,Long> {
    boolean existsByUser_IdEqualsAndType_IdEqualsAndDateEquals(Long id, Long id1, LocalDate date);
    @Query("insert into User values ()")
    void save(ExerciseNoteAdditionDTO exerciseNote);
}
