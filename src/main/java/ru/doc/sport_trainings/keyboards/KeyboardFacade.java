package ru.doc.sport_trainings.keyboards;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.doc.sport_trainings.DTO.ExerciseTypeDTO;

import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeyboardFacade {

    ReplyKeyboardMaker keyboardMaker;
    InlineKeyboardMaker inlineKeyboardMaker;
    public KeyboardFacade(ReplyKeyboardMaker keyboardMarkup, InlineKeyboardMaker inlineKeyboardMaker) {
        this.keyboardMaker = keyboardMarkup;
        this.inlineKeyboardMaker = inlineKeyboardMaker;
    }

    public ReplyKeyboardMarkup menuKeyboard() {
        return keyboardMaker.menuKeyboard();
    }
    public InlineKeyboardMarkup exerciseKeyboard(List<ExerciseTypeDTO> dto) {
        return inlineKeyboardMaker.getExercises(dto);
    }

    public List<KeyboardRow> addUserControlButton(ReplyKeyboardMarkup rkm) {
        return keyboardMaker.addUserControlButton(rkm);
    }
}
