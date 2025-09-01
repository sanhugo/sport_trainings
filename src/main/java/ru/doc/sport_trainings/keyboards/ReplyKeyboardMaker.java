package ru.doc.sport_trainings.keyboards;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplyKeyboardMaker {
    public ReplyKeyboardMarkup menuKeyboard() {
        ReplyKeyboardMarkup menu = new ReplyKeyboardMarkup();
        menu.setResizeKeyboard(true);
        menu.setOneTimeKeyboard(true);
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Добавить запись"));
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardButton newEx =  new KeyboardButton("Новый вид упражнения");
        KeyboardButton graph = new KeyboardButton("График упражнения");
        keyboardRow1.add(newEx);
        keyboardRow1.add(graph);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(keyboardRow);
        keyboardRows.add(keyboardRow1);
        menu.setKeyboard(keyboardRows);
        return menu;
    }

    public List<KeyboardRow> addUserControlButton(ReplyKeyboardMarkup rkm) {
        KeyboardRow admin_panel = new KeyboardRow();
        admin_panel.add(new KeyboardButton("Админ-панель"));
        List<KeyboardRow> rows = rkm.getKeyboard();
        rows.add(admin_panel);
        return rows;
    }
}
