package com.scribassu.scribabot.util;

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

    public static Map<String, String> getBotMessageForFullTimeLessons(List<FullTimeLesson> lessons, String day, String dayNumber) {
        Map<String, String> botMessage = new HashMap<>();
        botMessage.put(Constants.KEY_MESSAGE, Templates.makeTemplate(lessons, day, dayNumber));
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

    public static String getDayByDayNumber(String dayNumber) {
        switch(dayNumber) {
            case "1": return "пн.";
            case "2": return "вт.";
            case "3": return "ср.";
            case "4": return "чт.";
            case "5": return "пт.";
            case "6": return "сб.";
            case "7": return "вс.";
            default: return "";
        }
    }
}
