package com.scribassu.scribabot.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageHandlerImpl implements MessageHandler {
    @Override
    public Map<String, String> getBotMessage(String message) {
        System.out.println("Get message%3a " + message);
        Map<String, String> botMessage = new HashMap<>();

        if (message.equalsIgnoreCase("т"))
            botMessage.put("message", "test");

        if (message.equalsIgnoreCase("к")) {
            botMessage.put("message", "keybrd");
            botMessage.put("keyboard", k1);
        }

        return botMessage;
    }

    private static final String k1 = "%7b%22one_time%22%3afalse,%22buttons%22%3a%5b%5b%7b%22action%22%3a%7b%22type%22%3a%22text%22,%22payload%22%3a%22%7b%5c%22button%5c%22%3a%20%5c%221%5c%22%7d%22,%22label%22%3a%22Schedule%22%7d,%22color%22%3a%22primary%22%7d,%7b%22action%22%3a%7b%22type%22%3a%22text%22,%22payload%22%3a%22%7b%5c%22button%5c%22%3a%20%5c%221%5c%22%7d%22,%22label%22%3a%22Activities%22%7d,%22color%22%3a%22primary%22%7d%5d,%5b%7b%22action%22%3a%7b%22type%22%3a%22text%22,%22payload%22%3a%22%7b%5c%22button%5c%22%3a%20%5c%221%5c%22%7d%22,%22label%22%3a%22Reject%20lesson%22%7d,%22color%22%3a%22primary%22%7d,%7b%22action%22%3a%7b%22type%22%3a%22text%22,%22payload%22%3a%22%7b%5c%22button%5c%22%3a%20%5c%221%5c%22%7d%22,%22label%22%3a%22Add%20exam%22%7d,%22color%22%3a%22primary%22%7d%5d,%5b%7b%22action%22%3a%7b%22type%22%3a%22text%22,%22payload%22%3a%22%7b%5c%22button%5c%22%3a%20%5c%221%5c%22%7d%22,%22label%22%3a%22Enable%20notifications%22%7d,%22color%22%3a%22primary%22%7d%5d%5d%7d";
}
