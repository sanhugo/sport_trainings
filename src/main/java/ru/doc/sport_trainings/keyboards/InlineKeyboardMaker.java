package ru.doc.sport_trainings.keyboards;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.doc.sport_trainings.DTO.ExerciseTypeDTO;

import java.util.List;

@Component
public class InlineKeyboardMaker {

    public InlineKeyboardMarkup getExercises(List<ExerciseTypeDTO> dtos) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        for (ExerciseTypeDTO dto : dtos) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(dto.getName());
            button.setCallbackData(dto.getId().toString());
        }
    }
}
