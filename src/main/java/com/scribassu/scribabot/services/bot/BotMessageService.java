package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.InnerBotUser;

public interface BotMessageService {
    BotMessage getBotMessage(String message, InnerBotUser botUser);
}
