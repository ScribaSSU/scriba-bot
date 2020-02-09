package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.commands.CommandText;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.tracto.domain.EducationForm;
import com.scribassu.tracto.domain.FullTimeLesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class FullTimeLessonService implements BotMessageService {

    private final CallRestService callRestService;

    @Autowired
    public FullTimeLessonService(CallRestService callRestService) {
        this.callRestService = callRestService;
    }

    @Override
    public Map<String, String> getBotMessage(String message, BotUser botUser) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Samara"));
        List<FullTimeLesson> lessons = new ArrayList<>();
        boolean isBotUserFullTime = false;

        switch(message) {
            case CommandText.MONDAY:
                if(isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "1"
                    );
                    isBotUserFullTime = true;
                }
                break;
            case CommandText.TUESDAY:
                if(isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "2"
                    );
                    isBotUserFullTime = true;
                }
                break;
            case CommandText.WEDNESDAY:
                if(isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "3"
                    );
                    isBotUserFullTime = true;
                }
                break;
            case CommandText.THURSDAY:
                if(isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "4"
                    );
                    isBotUserFullTime = true;
                }
                break;
            case CommandText.FRIDAY:
                if(isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "5"
                    );
                    isBotUserFullTime = true;
                }
                break;
            case CommandText.SATURDAY:
                if(isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            "6"
                    );
                    isBotUserFullTime = true;
                }
                break;
            case CommandText.TODAY:
                if(isBotUserFullTime(botUser)) {
                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            String.valueOf(getDayOfWeekStartsFromMonday(calendar))
                    );
                    isBotUserFullTime = true;
                }
                break;
            case CommandText.TOMORROW:
                calendar.add(Calendar.DAY_OF_WEEK, 1);
                if(isBotUserFullTime(botUser)) {

                    lessons = callRestService.getFullTimeLessonsByDay(
                            botUser.getDepartment(),
                            botUser.getGroupNumber(),
                            String.valueOf(getDayOfWeekStartsFromMonday(calendar))
                    );
                    isBotUserFullTime = true;
                }
                break;
        }

        if(isBotUserFullTime) {
            return BotMessageUtils.getBotMessageForFullTimeLessons(lessons);
        }
        else {
            return BotMessageUtils.getBotMessageForUnsupportedLessons();
        }
    }

    private boolean isBotUserFullTime(BotUser botUser) {
        return botUser != null
                && botUser.getEducationForm() != null
                && EducationForm.DO.getGroupType().equalsIgnoreCase(botUser.getEducationForm())
                && !StringUtils.isEmpty(botUser.getGroupNumber());
    }

    private int getDayOfWeekStartsFromMonday(Calendar calendar) {
        int dayOfWeekStartsFromMonday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if(dayOfWeekStartsFromMonday == 0) {
            dayOfWeekStartsFromMonday = 7;
        }
        return dayOfWeekStartsFromMonday;
    }
}
