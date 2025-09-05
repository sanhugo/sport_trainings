package ru.doc.sport_trainings.keyboards;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.doc.sport_trainings.DTO.ExerciseTypeDTO;
import ru.doc.sport_trainings.service.ExerciseService;

import java.util.ArrayList;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class InlineKeyboardMaker {
    ExerciseService exservice;

    public InlineKeyboardMaker(ExerciseService exservice) {
        this.exservice = exservice;
    }

    public InlineKeyboardMarkup getExercises() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<ExerciseTypeDTO> dtos = exservice.getExerciseTypes();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (ExerciseTypeDTO dto : dtos) {
            List<InlineKeyboardButton> keyboard  = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(dto.getName());
            button.setCallbackData(dto.getId().toString());
            keyboard.add(button);
            rows.add(keyboard);
        }
        markup.setKeyboard(rows);
        return markup;
    }
}
