package com.scribassu.scribabot.model;

import com.scribassu.scribabot.util.BotUserSource;
import lombok.Data;

@Data
public class Command {
    private final String message;
    private final String payload;
    private final String userId;
    private final BotUserSource botUserSource;
}
