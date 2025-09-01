package ru.doc.sport_trainings.bot;

import lombok.Getter;

@Getter
public enum ButtonsName {
    ADD_TRAINING("Новая тренировка"),
    GET_GRAPHICS("Построить график"),
    ADD_NEW_EXERCISE("Новое упражнение");

    private final String message;

    ButtonsName(String message) {
        this.message = message;
    }
}
