package ru.doc.sport_trainings.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

/**
 * DTO for {@link ru.doc.sport_trainings.model.ExerciseNote}
 */
@AllArgsConstructor
@Getter
@Setter
public class ExerciseNoteGraphDTO {
    private LocalDate date;
    private Double times;

    public ExerciseNoteGraphDTO(Date sqlDate, double times) {
        this.date = sqlDate.toLocalDate();
        this.times = times;
    }
}