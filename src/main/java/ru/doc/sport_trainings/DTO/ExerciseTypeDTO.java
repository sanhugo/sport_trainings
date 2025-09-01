package ru.doc.sport_trainings.DTO;

import lombok.Value;

/**
 * DTO for {@link ru.doc.sport_trainings.model.ExerciseType}
 */
@Value
public class ExerciseTypeDTO {
    Long id;
    String name;
}