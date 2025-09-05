package ru.doc.sport_trainings.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.doc.sport_trainings.DTO.ExerciseNoteAdditionDTO;
import ru.doc.sport_trainings.bot.BotStatus;
import ru.doc.sport_trainings.checker.RegexChecker;
import ru.doc.sport_trainings.keyboards.KeyboardFacade;
import ru.doc.sport_trainings.service.ExerciseNoteService;
import ru.doc.sport_trainings.service.ExerciseService;
import ru.doc.sport_trainings.service.UserService;
import ru.doc.sport_trainings.service.UserStateService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageHandler {
    KeyboardFacade keyboard;
    UserStateService stateService;
    RegexChecker checker;
    ExerciseNoteService exerciseNoteService;
    ExerciseService es;
    UserService userService;

    public MessageHandler(KeyboardFacade keyboard, UserStateService stateService, RegexChecker checker, ExerciseNoteService exerciseNoteService, ExerciseService es, UserService userService) {
        this.keyboard = keyboard;
        this.stateService = stateService;
        this.checker = checker;
        this.exerciseNoteService = exerciseNoteService;
        this.es = es;
        this.userService = userService;
    }

    public BotApiMethod<?> execute(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(update.getMessage().getChatId());
        BotStatus status = stateService.getState(update.getMessage().getChatId());
        switch(status) {
            case READY:
        switch(update.getMessage().getText())
        {
            case "Новый вид упражнения":
                sendMessage.setText("Введите название упражнения");
                stateService.changeUserStatus(update.getMessage().getChatId(), BotStatus.NEW_EXERCISE);
                break;
            case "График упражнения":
                sendMessage.setText("Выберите упражнение");
                stateService.changeUserStatus(update.getMessage().getChatId(), BotStatus.GRAPH_WAITING);
                sendMessage.setReplyMarkup(keyboard.exerciseKeyboard());
                break;
            case "Добавить запись":
                sendMessage.setText("Выберите упражнение");
                stateService.changeUserStatus(update.getMessage().getChatId(), BotStatus.EVENT_WAITING);
                System.out.println(stateService.getState(update.getMessage().getChatId()));
                sendMessage.setReplyMarkup(keyboard.exerciseKeyboard());
                break;
            default: sendMessage.setText("Неверная команда. Выберите еще раз.");
            sendMessage.setReplyMarkup(keyboard.menuKeyboard());
            break;
        }
        break;
            case EVENT_WAITING:
                boolean checkExerciseid = stateService.checkExercise(update.getMessage().getChatId());
                if (checkExerciseid)
                {
                    boolean checkFormat = checker.checkforNumber(update.getMessage().getText());
                    if (checkFormat)
                    {
                        String num = update.getMessage().getText();
                        num=num.replace(',','.');
                        double number = Double.parseDouble(num);
                        ExerciseNoteAdditionDTO dto = exerciseNoteService.getExerciseNote(update.getMessage().getChatId(), number);
                        boolean isAdded = exerciseNoteService.createExerciseNote(dto);
                        if (isAdded){
                        sendMessage.setText("Успешно добавлено!");
                        }
                        else
                        {
                            sendMessage.setText("Извините, произошла ошибка при добавлении показателя");
                        }
                        stateService.changeUserStatus(update.getMessage().getChatId(), BotStatus.READY);
                        sendMessage.setReplyMarkup(keyboard.menuKeyboard());
                    }
                    else
                    {
                        sendMessage.setText("Введите только одно число");
                    }
                }
                else
                {
                    sendMessage.setText("Выберите упражнение на клавиатуре.");
                }
                break;
            case NEW_EXERCISE:
                String text = update.getMessage().getText();
                boolean addedOrNot = es.addExerciseType(text);
                if (addedOrNot)
                {
                    sendMessage.setText("Новое упражнение успешно добавлено!");
                    stateService.changeUserStatus(update.getMessage().getChatId(), BotStatus.READY);
                    sendMessage.setReplyMarkup(keyboard.menuKeyboard());
                }
                else
                {
                    sendMessage.setText("Упражнение не добавлено.");
                }
                break;
                case GRAPH_WAITING:
                    boolean checkExercise = stateService.checkExercise(update.getMessage().getChatId());
                    if (!checkExercise)
                    {
                        sendMessage.setText("Выберите упражнение на клавиатуре.");
                    }
                    break;
            case null:
                stateService.newState(update.getMessage().getChatId(), BotStatus.READY);
                ReplyKeyboardMarkup rkm =keyboard.menuKeyboard();
                if (userService.checkAdmin(update.getMessage().getChatId()))
                {
                    rkm.setKeyboard(keyboard.addUserControlButton(rkm));
                }
                sendMessage.setReplyMarkup(rkm);
                sendMessage.setText("Бот аварийно завершил работу. Выберите пункт главного меню");
        }
        return sendMessage;
    }

}
