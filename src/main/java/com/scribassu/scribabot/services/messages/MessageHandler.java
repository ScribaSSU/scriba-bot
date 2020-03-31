package com.scribassu.scribabot.services.messages;

import com.scribassu.scribabot.text.Command;

import java.util.Map;

public interface MessageHandler {
    Map<String, String> getBotMessage(Command command);
}
