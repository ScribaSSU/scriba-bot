package com.scribassu.scribabot.util;

import com.scribassu.tracto.domain.FullTimeLesson;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotMessageUtils {

    public static Map<String, String> getBotMessageForFullTimeLessons(List<FullTimeLesson> lessons) {
        Map<String, String> botMessage = new HashMap<>();
        if(CollectionUtils.isEmpty(lessons)) {
            botMessage.put(Constants.KEY_MESSAGE, "Информация отсутствует.");
        }
        else {
            botMessage.put(Constants.KEY_MESSAGE, Templates.makeTemplate(lessons));
        }
        return botMessage;
    }
}
