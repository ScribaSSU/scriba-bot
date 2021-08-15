package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.InnerBotUser;
import com.scribassu.scribabot.entities.BotUser;

public interface BotMessageService {
    BotMessage getBotMessage(String message, InnerBotUser botUser);
}
