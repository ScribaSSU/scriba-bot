package com.scribassu.scribabot.model;

import com.scribassu.scribabot.model.inner_keyboard.InnerKeyboard;
import com.scribassu.scribabot.text.MessageText;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@Data
@NoArgsConstructor
public class BotMessage {

    private String message;

    private Optional<InnerKeyboard> innerKeyboard;
    private InnerBotUser botUser;

    public BotMessage(String message, InnerBotUser innerBotUser) {
        this.message = message;
        this.innerKeyboard = Optional.empty();
        this.botUser = innerBotUser;
    }

    public BotMessage(String message, InnerKeyboard innerKeyboard, InnerBotUser innerBotUser) {
        this.message = message;
        this.innerKeyboard = Optional.of(innerKeyboard);
        this.botUser = innerBotUser;
    }

    public void setKeyboard(InnerKeyboard innerKeyboard) {
        this.innerKeyboard = Optional.of(innerKeyboard);
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

