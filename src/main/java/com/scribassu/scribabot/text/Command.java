package com.scribassu.scribabot.text;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Command {

    private String message;
    private String payload;
    private String userId;

    public Command(String message) {
        this.message = message;
    }

    public Command(String message, String payload, String userId) {
        this.message = message;
        this.payload = payload;
        this.userId = userId;
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
