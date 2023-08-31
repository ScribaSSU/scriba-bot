package com.scribassu.scribabot.model;

import lombok.Data;

@Data
public class Command {
    private final String message;
    private final String userId;
    private final BotUserSource botUserSource;
}
