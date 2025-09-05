package ru.doc.sport_trainings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.doc.sport_trainings.DTO.ExerciseNoteAdditionDTO;
import ru.doc.sport_trainings.DTO.ExerciseNoteGraphDTO;
import ru.doc.sport_trainings.model.ExerciseNote;

import java.time.LocalDate;
import java.util.List;

public interface ExerciseNoteRepository extends JpaRepository<ExerciseNote,Long> {
    boolean existsByUser_IdEqualsAndType_IdEqualsAndDateEquals(Long id, Long id1, LocalDate date);
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO exercise_notes (users_id, exercise_types_id, dates, times) " +
            "VALUES (:#{#dto.userId}, :#{#dto.typeId}, :date, :#{#dto.times})", nativeQuery = true)
    void insertExerciseNoteFromDTO(@Param("dto") ExerciseNoteAdditionDTO dto,
                                   @Param("date") LocalDate date);

    boolean existsByUser_IdAndType_IdAndDate(Long id, Long id1, LocalDate date);

    @Query("select new ru.doc.sport_trainings.DTO.ExerciseNoteGraphDTO(e.date, e.times) " +
            "from ExerciseNote e " +
            "where e.user.id = :userid and e.type.id = :exid")
    List<ExerciseNoteGraphDTO> allDTOByUserIDandExerciseID(@Param("userid") long id,
                                                           @Param("exid") long exerciseId);

}
