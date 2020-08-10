package com.scribassu.scribabot.services.messages;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.Command;

public interface MessageHandler {
    BotMessage getBotMessage(Command command);
}
