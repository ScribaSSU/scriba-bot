package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.entities.BotUser;

import java.util.Map;

public interface BotMessageService {
    Map<String, String> getBotMessage(String message, BotUser botUser);
}
