package com.scribassu.scribabot.dto;

import com.scribassu.scribabot.util.BotUserSource;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Command {

    private String message;
    private String payload;
    private String userId;
    private BotUserSource botUserSource;

    public Command(String message) {
        this.message = message;
    }

    public Command(String message, String payload, String userId, BotUserSource botUserSource) {
        this.message = message;
        this.payload = payload;
        this.userId = userId;
        this.botUserSource = botUserSource;
    }

    @Override
    public String toString() {
        return "Command{" +
                "message='" + message + '\'' +
                ", payload='" + payload + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
