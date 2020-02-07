package com.scribassu.scribabot.services.messages;

import java.util.Map;

public interface MessageSender {
    void send(Map<String, String> botMessage, String userId) throws Exception;
}
