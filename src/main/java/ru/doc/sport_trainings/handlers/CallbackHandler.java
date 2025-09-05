package ru.doc.sport_trainings.handlers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jfree.chart.JFreeChart;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.doc.sport_trainings.DTO.ExerciseNoteGraphDTO;
import ru.doc.sport_trainings.bot.BotStatus;
import ru.doc.sport_trainings.bot.SportBot;
import ru.doc.sport_trainings.graphics.GraphicsBuilder;
import ru.doc.sport_trainings.graphics.PhotoGraph;
import ru.doc.sport_trainings.keyboards.KeyboardFacade;
import ru.doc.sport_trainings.service.ExerciseNoteService;
import ru.doc.sport_trainings.service.ExerciseService;
import ru.doc.sport_trainings.service.UserStateService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CallbackHandler {
    UserStateService  userStateService;
    ExerciseNoteService exerciseService;
    RedisTemplate<String,Object> redisTemplate;
    GraphicsBuilder gb;
    ExerciseService et;
    KeyboardFacade kf;
    public CallbackHandler(UserStateService userStateService, ExerciseNoteService exerciseService, RedisTemplate<String, Object> redisTemplate, GraphicsBuilder gb, ExerciseService et, KeyboardFacade kf) {
        this.userStateService = userStateService;
        this.exerciseService = exerciseService;
        this.redisTemplate = redisTemplate;
        this.gb = gb;
        this.et = et;
        this.kf = kf;
    }

    public BotApiMethod<?> work(Update update,SportBot bot) throws TelegramApiException, IOException {
        int updateMessage = update.getCallbackQuery().getMessage().getMessageId();
        long userID = update.getCallbackQuery().getMessage().getChatId();
        long exerciseID = Long.parseLong(update.getCallbackQuery().getData());
        DeleteMessage delete = new DeleteMessage();
        delete.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        delete.setMessageId(updateMessage);
        try{
            bot.execute(delete);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
        userStateService.addExerciseId(userID,exerciseID);
        String key = "Users:"+userID;
        BotStatus check = userStateService.getState(userID);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        if (check.equals(BotStatus.GRAPH_WAITING))
        {
            long exerciseId=Long.parseLong(redisTemplate.opsForHash().get(key,"exerciseId").toString());
            List<ExerciseNoteGraphDTO> dto=exerciseService.getNotesByExerciseType(userID,exerciseId);
            if (dto.isEmpty())
            {
                sendMessage.setText("Нет данных для формирования графика");
            }
            else {
                String nameForChart = et.getExerciseType(exerciseId);
                JFreeChart chart = gb.createChart(dto,nameForChart);
                byte[] image = PhotoGraph.toPNG(chart, 1280, 720);

                InputStream inputStream = new ByteArrayInputStream(image);
                InputFile inputFile = new InputFile(inputStream, "chart.png");

                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(userID);
                sendPhoto.setPhoto(inputFile);

                bot.execute(sendPhoto);
                sendMessage.enableMarkdown(true);
                sendMessage.setText("График упражнения "+nameForChart);
            }
            redisTemplate.opsForHash().put(key,"status",BotStatus.READY.name());
            sendMessage.setReplyMarkup(kf.menuKeyboard());
        }
        else if (check.equals(BotStatus.EVENT_WAITING))
        {
            sendMessage.enableMarkdown(true);
            sendMessage.setText("Введите показатель:");
        }
        return sendMessage;
    }
}
