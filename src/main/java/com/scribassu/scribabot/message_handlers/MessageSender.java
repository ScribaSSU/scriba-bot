package com.scribassu.scribabot.message_handlers;

import com.scribassu.scribabot.model.BotMessage;

public interface MessageSender {
    void send(BotMessage botMessage) throws Exception;
}
