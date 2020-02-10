package com.scribassu.scribabot.services.messages;

import java.util.Map;

public interface MessageHandler {
    Map<String, String> getBotMessage(String message, String userId);
}
