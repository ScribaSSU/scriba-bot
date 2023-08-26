package com.scribassu.scribabot.services.messages;

import com.google.gson.*;
import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// todo to delete
@Slf4j
@Service
public class MessageParserImpl {

    private final Gson gson = new GsonBuilder().create();

    public Map<String, String> parseMessage(String incomingJson) {
        log.info("VK Message: " + incomingJson);
        Map<String, String> parsed = new HashMap<>();
        JsonObject requestJson = gson.fromJson(incomingJson, JsonObject.class);
        String type = requestJson.get(Constants.KEY_TYPE).getAsString();

        if (type.equalsIgnoreCase(Constants.TYPE_CONFIRMATION)) {
            System.out.println(Constants.TYPE_CONFIRMATION);
            parsed.put(Constants.TYPE_CONFIRMATION, "y");
            return parsed;
        }
        if (type.equalsIgnoreCase(Constants.TYPE_MESSAGE_NEW)) {
            JsonObject object = requestJson.getAsJsonObject(Constants.KEY_OBJECT);
            String userId = object.getAsJsonPrimitive(Constants.KEY_PEER_ID).getAsString();
            String message = object.getAsJsonPrimitive(Constants.KEY_TEXT).getAsString();
            JsonArray attachments = object.getAsJsonArray(Constants.KEY_ATTACHMENTS);
            Iterator<JsonElement> attachmentsItems = attachments.iterator();
            if (attachmentsItems.hasNext()) {
                JsonObject attachmentItem = attachmentsItems.next().getAsJsonObject();
                String attachmentItemType = attachmentItem.getAsJsonPrimitive(Constants.KEY_TYPE).getAsString();
                if (Constants.KEY_STICKER.equals(attachmentItemType)) {
                    message = CommandText.STICKER_WAS_SENT_TO_BOT;
                }
            }
            parsed.put(Constants.KEY_MESSAGE, message);
            parsed.put(Constants.KEY_USER_ID, userId);

            if (object.has(Constants.KEY_PAYLOAD)) {
                String payload = object.getAsJsonPrimitive(Constants.KEY_PAYLOAD).getAsString();
                if (payload.startsWith(Constants.PAYLOAD_START) && payload.endsWith(Constants.PAYLOAD_END)) {
                    payload = payload.substring(Constants.PAYLOAD_START.length());
                    payload = payload.substring(0, payload.indexOf(Constants.PAYLOAD_END));
                    parsed.put(Constants.KEY_PAYLOAD, payload);
                }
            }

            return parsed;
        }
        return new HashMap<>();
    }
}
