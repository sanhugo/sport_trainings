package ru.doc.sport_trainings.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link ru.doc.sport_trainings.model.ExerciseType}
 */
@AllArgsConstructor
@Getter
@Setter
public class ExerciseTypeDTO {
    Long id;
    String name;
}