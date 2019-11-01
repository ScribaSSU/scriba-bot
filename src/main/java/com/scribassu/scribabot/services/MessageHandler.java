package com.scribassu.scribabot.services;

import java.util.Map;

public interface MessageHandler {
    Map<String, String> getBotMessage(String message);
}
