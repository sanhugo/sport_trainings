package ru.doc.sport_trainings.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link ru.doc.sport_trainings.model.ExerciseNote}
 */
@AllArgsConstructor
@Getter
@Setter
public class ExerciseNoteAdditionDTO {
    private Long userId;
    private Long typeId;
    private Integer times;
}