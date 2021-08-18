package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.InnerBotUser;
import com.scribassu.scribabot.dto.rest.ExtramuralDto;
import com.scribassu.scribabot.services.CallRestService;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.BotMessageUtils;
import com.scribassu.scribabot.util.CalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class ExtramuralEventService implements BotMessageService {

    private final CallRestService callRestService;

    @Autowired
    public ExtramuralEventService(CallRestService callRestService) {
        this.callRestService = callRestService;
    }

    @Override
    public BotMessage getBotMessage(String message, InnerBotUser botUser) {
        Calendar calendar = CalendarUtils.getCalendar();
        ExtramuralDto lessons = new ExtramuralDto();
        boolean isToday = false;
        boolean isTomorrow = false;
        boolean isYesterday = false;

        switch (message) {
            case CommandText.ALL_LESSONS:
                if (BotMessageUtils.isBotUserExtramural(botUser)) {
                    lessons = callRestService.getExtramuralEventsByGroup(
                            botUser.getDepartment(),
                            botUser.getGroupNumber()
                    );
                }
                break;
            case CommandText.TODAY:
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                if (BotMessageUtils.isBotUserExtramural(botUser)) {
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
                if (BotMessageUtils.isBotUserExtramural(botUser)) {
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
                if (BotMessageUtils.isBotUserExtramural(botUser)) {
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
            return BotMessageUtils.getBotMessageForEmptyExtramuralEvents(botUser);
        } else {
            if (isToday) {
                return BotMessageUtils.getBotMessageForExtramuralEvent(lessons, CommandText.TODAY, botUser);
            }
            if (isTomorrow) {
                return BotMessageUtils.getBotMessageForExtramuralEvent(lessons, CommandText.TOMORROW, botUser);
            }
            if (isYesterday) {
                return BotMessageUtils.getBotMessageForExtramuralEvent(lessons, CommandText.YESTERDAY, botUser);
            }
            return BotMessageUtils.getBotMessageForExtramuralEvent(lessons, "", botUser);
        }
    }
}