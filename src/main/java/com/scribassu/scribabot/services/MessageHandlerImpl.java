package com.scribassu.scribabot.services;

import com.scribassu.scribabot.keyboard.KeyboardMap;
import com.scribassu.scribabot.keyboard.KeyboardType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageHandlerImpl implements MessageHandler {

    @Autowired
    private SymbolConverter symbolConverter;

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

        return botMessage;
    }
}
