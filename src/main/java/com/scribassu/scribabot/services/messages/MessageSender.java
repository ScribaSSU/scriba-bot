package com.scribassu.scribabot.services.messages;

import com.scribassu.scribabot.model.BotMessage;

public interface MessageSender {
    void send(BotMessage botMessage) throws Exception;
}
