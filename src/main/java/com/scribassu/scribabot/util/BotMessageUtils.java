package com.scribassu.scribabot.util;

import com.scribassu.scribabot.dto.FullTimeLessonDto;
import com.scribassu.scribabot.entities.BotUser;
import com.scribassu.scribabot.keyboard.KeyboardMap;
import com.scribassu.scribabot.keyboard.KeyboardType;
import com.scribassu.tracto.domain.EducationForm;
import com.scribassu.tracto.domain.FullTimeLesson;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotMessageUtils {

    public static Map<String, String> getBotMessageForFullTimeLessons(FullTimeLessonDto fullTimeLessonDto,
                                                                      String day) {
        Map<String, String> botMessage = new HashMap<>();
        botMessage.put(Constants.KEY_MESSAGE, Templates.makeTemplate(fullTimeLessonDto, day));
        botMessage.put(
                Constants.KEY_KEYBOARD,
                KeyboardMap.keyboards.get(KeyboardType.ButtonSchedule).getJsonText());
        return botMessage;
    }

    public static Map<String, String> getBotMessageForUnsupportedLessons() {
        Map<String, String> botMessage = new HashMap<>();
        botMessage.put(Constants.KEY_MESSAGE, "Ваш вид расписания пока не поддерживается или вы указали недостаточно информации для выдачи расписания");
        botMessage.put(Constants.KEY_KEYBOARD, KeyboardMap.keyboards.get(KeyboardType.ButtonSchedule).getJsonText());
        return botMessage;
    }

    public static boolean isBotUserFullTime(BotUser botUser) {
        return botUser != null
                && botUser.getEducationForm() != null
                && EducationForm.DO.getGroupType().equalsIgnoreCase(botUser.getEducationForm())
                && !StringUtils.isEmpty(botUser.getGroupNumber());
    }
}
