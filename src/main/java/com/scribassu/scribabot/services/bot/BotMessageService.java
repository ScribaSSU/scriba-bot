package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.entities.BotUser;

import java.util.Map;

public interface BotMessageService {
    BotMessage getBotMessage(String message, BotUser botUser);
}
