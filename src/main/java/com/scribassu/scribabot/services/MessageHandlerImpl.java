package com.scribassu.scribabot.services;

import com.scribassu.scribabot.keyboard.KeyboardMap;
import com.scribassu.scribabot.keyboard.KeyboardType;
import com.scribassu.scribabot.util.Templates;
import com.scribassu.tracto.domain.FullTimeLesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageHandlerImpl implements MessageHandler {

    @Autowired
    private SymbolConverter symbolConverter;

    @Autowired
    private CallRestService callRestService;

    @Override
    public Map<String, String> getBotMessage(String message) {
        System.out.println("Get message: " + message);
        Map<String, String> botMessage = new HashMap<>();

        if (message.equalsIgnoreCase("т"))
            botMessage.put("message", "test");

        if (message.equalsIgnoreCase("к")) {
            botMessage.put("message", "keybrd");
            botMessage.put("keyboard", KeyboardMap.keyboards.get(KeyboardType.ButtonActions).getJsonText());
        }

        if(message.startsWith("р")) {
            String[] params = message.split(" ");
            List<FullTimeLesson> lessons = callRestService.getFullTimeLessonsByDay(
                    params[1],
                    params[2],
                    params[3]
            );
            botMessage.put("message", Templates.makeTemplate(lessons));
        }

        return botMessage;
    }
}
