package com.scribassu.scribabot.dto;

import com.scribassu.scribabot.keyboard.KeyboardMap;
import com.scribassu.scribabot.keyboard.KeyboardType;
import com.scribassu.scribabot.text.MessageText;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@NoArgsConstructor
public class BotMessage {

    private String message;

    private String keyboard;

    public BotMessage(String message) {
        this.message = message;
    }

    public BotMessage(String message, KeyboardType keyboardType) {
        this.message = message;
        setKeyboard(keyboardType);
    }

    public BotMessage(String message, String keyboard) {
        this.message = message;
        this.keyboard = keyboard;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setKeyboard(KeyboardType keyboardType) {
        this.keyboard = KeyboardMap.get(keyboardType).getJsonText();
    }

    public void formatKeyboard(String replacedString, KeyboardType keyboardType) {
        this.keyboard = this.keyboard.replace(replacedString, KeyboardMap.get(keyboardType).getJsonText());
    }

    public boolean isEmpty() {
        return StringUtils.isEmpty(message);
    }

    public boolean isDefault() {
        return message.equalsIgnoreCase(MessageText.DEFAULT_MESSAGE)
                && keyboard.equalsIgnoreCase(KeyboardMap.get(KeyboardType.ButtonActions).getJsonText());
    }

    public boolean hasKeyboard() {
        return StringUtils.isNotEmpty(keyboard);
    }
}

