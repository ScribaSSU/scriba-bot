package com.scribassu.scribabot.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.scribassu.scribabot.util.Constants;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageParserImpl implements MessageParser {

    private Gson gson = new GsonBuilder().create();

    @Override
    public Map<String, String> parseMessage(String incomingJson) {
        System.out.println(incomingJson);
        Map<String, String> parsed = new HashMap<>();

        JsonObject requestJson = gson.fromJson(incomingJson, JsonObject.class);

        String type = requestJson.get("type").getAsString();

        if(type.equalsIgnoreCase(Constants.TYPE_CONFIRMATION)) {
            System.out.println(Constants.TYPE_CONFIRMATION);
            parsed.put(Constants.TYPE_CONFIRMATION, "y");
            return parsed;
        }
        if(type.equalsIgnoreCase(Constants.TYPE_MESSAGE_NEW)) {
            JsonObject object = requestJson.getAsJsonObject(Constants.KEY_OBJECT);
            String userId = object.getAsJsonPrimitive(Constants.KEY_FROM_ID).getAsString();
            String message = object.getAsJsonPrimitive(Constants.KEY_TEXT).getAsString();
            parsed.put(Constants.KEY_MESSAGE, message);
            parsed.put(Constants.KEY_USER_ID, userId);
            System.out.println(userId + " message " + message);
            return parsed;
        }
        return new HashMap<>();
    }
}
