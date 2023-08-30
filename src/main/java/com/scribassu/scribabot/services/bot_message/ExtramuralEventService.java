package com.scribassu.scribabot.services.bot_message;

import com.scribassu.scribabot.dto.rest.ExtramuralDto;
import com.scribassu.scribabot.dto.rest.TeacherExtramuralEventDto;
import com.scribassu.scribabot.generators.BotMessageGenerator;
import com.scribassu.scribabot.model.BotMessage;
import com.scribassu.scribabot.model.BotUser;
import com.scribassu.scribabot.services.BotMessageService;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.CalendarUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.concurrent.CompletableFuture;


// todo fix & refactor
@Service
@Slf4j
@Data
public class ExtramuralEventService implements BotMessageService {

    private final CallRestService callRestService;
    private final BotMessageGenerator botMessageGenerator;

    @Override
    public CompletableFuture<BotMessage> getBotMessage(String message, BotUser botUser) {
        if (botUser.wantTeacherSchedule()) {
            return getTeacherBotMessage(message, botUser);
        } else {
            return getStudentBotMessage(message, botUser);
        }
    }

    @Override
    public boolean shouldAccept(String message, BotUser botUser) {
        switch (message) {
            case CommandText.ALL_LESSONS:
            case CommandText.TODAY:
            case CommandText.TOMORROW:
            case CommandText.YESTERDAY:
                return BotUser.isBotUserExtramural(botUser);
            default: return false;
        }
    }

    private CompletableFuture<BotMessage> getTeacherBotMessage(String message, BotUser botUser) {
        String teacherId = botUser.getPreviousUserMessage().split(" ")[1];
        TeacherExtramuralEventDto lessons = callRestService.getTeacherExtramuralEvents(teacherId);

        if (null == lessons || null == lessons.getExtramuralEvents() || lessons.getExtramuralEvents().isEmpty()) {
            return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForEmptyExtramuralEventTeacher(botUser));
        }
        return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForExtramuralEventTeacher(lessons, botUser));
    }

    private CompletableFuture<BotMessage> getStudentBotMessage(String message, BotUser botUser) {
        Calendar calendar = CalendarUtils.getCalendar();
        ExtramuralDto lessons = new ExtramuralDto();
        boolean isToday = false;
        boolean isTomorrow = false;
        boolean isYesterday = false;

        switch (message) {
            case CommandText.ALL_LESSONS:
                if (BotUser.isBotUserExtramural(botUser)) {
                    lessons = callRestService.getExtramuralEventsByGroup(
                            botUser.getDepartment(),
                            botUser.getGroupNumber()
                    );
                }
                break;
            case CommandText.TODAY:
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                if (BotUser.isBotUserExtramural(botUser)) {
                    lessons = callRestService.getExtramuralEventsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            month,
                            day
                    );
                    isToday = true;
                }
                break;
            case CommandText.TOMORROW:
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                month = calendar.get(Calendar.MONTH) + 1;
                day = calendar.get(Calendar.DAY_OF_MONTH);
                if (BotUser.isBotUserExtramural(botUser)) {
                    lessons = callRestService.getExtramuralEventsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            month,
                            day
                    );
                    isTomorrow = true;
                }
                break;
            case CommandText.YESTERDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                month = calendar.get(Calendar.MONTH) + 1;
                day = calendar.get(Calendar.DAY_OF_MONTH);
                if (BotUser.isBotUserExtramural(botUser)) {
                    lessons = callRestService.getExtramuralEventsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            month,
                            day
                    );
                    isYesterday = true;
                }
                break;
        }

        if (null == lessons || null == lessons.getExtramuralEvents() || lessons.getExtramuralEvents().isEmpty()) {
            return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForEmptyExtramuralEvent(botUser));
        } else {
            if (isToday) {
                return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForExtramuralEvent(lessons, CommandText.TODAY, botUser));
            }
            if (isTomorrow) {
                return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForExtramuralEvent(lessons, CommandText.TOMORROW, botUser));
            }
            if (isYesterday) {
                return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForExtramuralEvent(lessons, CommandText.YESTERDAY, botUser));
            }
            return CompletableFuture.completedFuture(botMessageGenerator.getBotMessageForExtramuralEvent(lessons, "", botUser));
        }
    }
}