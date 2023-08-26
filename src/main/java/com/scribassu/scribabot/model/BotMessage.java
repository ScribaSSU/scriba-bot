package com.scribassu.scribabot.model;

import com.scribassu.scribabot.model.keyboard.Keyboard;
import com.scribassu.scribabot.text.MessageText;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@Data
@NoArgsConstructor
public class BotMessage {

    private String message;

    private Optional<Keyboard> innerKeyboard;
    private BotUser botUser;

    public BotMessage(String message, BotUser botUser) {
        this.message = message;
        this.innerKeyboard = Optional.empty();
        this.botUser = botUser;
    }

    public BotMessage(String message, Keyboard keyboard, BotUser botUser) {
        this.message = message;
        this.innerKeyboard = Optional.of(keyboard);
        this.botUser = botUser;
    }

    public void setKeyboard(Keyboard keyboard) {
        this.innerKeyboard = Optional.of(keyboard);
    }

    public boolean isEmpty() {
        return StringUtils.isEmpty(this.message);
    }

    public boolean isDefault() {
        return this.message.equalsIgnoreCase(MessageText.DEFAULT_MESSAGE);
    }

    public boolean hasKeyboard() {
        return this.innerKeyboard.isPresent();
    }
}

