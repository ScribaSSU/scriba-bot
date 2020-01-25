package com.scribassu.scribabot.services.messages;

import java.util.Map;

public interface MessageParser {
    /*
    Key: property name
    Value: property
     */
    Map<String, String> parseMessage(String incomingJson);
}
