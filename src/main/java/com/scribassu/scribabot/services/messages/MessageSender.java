package com.scribassu.scribabot.services.messages;

import com.scribassu.scribabot.dto.BotMessage;

public interface MessageSender {
    void send(BotMessage botMessage, String userId) throws Exception;
}
