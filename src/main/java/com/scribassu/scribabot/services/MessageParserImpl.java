package com.scribassu.scribabot.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageParserImpl implements MessageParser {

    private Gson gson = new GsonBuilder().create();

    public final static String CONFIRMATION_TYPE = "confirmation";
    public final static String MESSAGE_NEW_TYPE = "message_new";

    @Override
    public Map<String, String> parseMessage(String incomingJson) {
        System.out.println(incomingJson);
        Map<String, String> parsed = new HashMap<>();

        JsonObject requestJson = gson.fromJson(incomingJson, JsonObject.class);

        String type = requestJson.get("type").getAsString();

        if(type.equalsIgnoreCase(CONFIRMATION_TYPE)) {
            System.out.println(CONFIRMATION_TYPE);
            parsed.put(CONFIRMATION_TYPE, "y");
            return parsed;
        }
        if(type.equalsIgnoreCase(MESSAGE_NEW_TYPE)) {
            JsonObject object = requestJson.getAsJsonObject("object");
            String userId = object.getAsJsonPrimitive("from_id").getAsString();
            String message = object.getAsJsonPrimitive("text").getAsString();
            parsed.put("message", message);
            parsed.put("userId", userId);
            System.out.println(userId + " message " + message);
            return parsed;
        }
        return new HashMap<>();
    }
}
