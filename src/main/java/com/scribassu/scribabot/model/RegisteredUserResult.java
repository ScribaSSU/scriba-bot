package com.scribassu.scribabot.model;

import lombok.Data;

@Data
public class RegisteredUserResult {
    private final boolean isRegistered;
    private final InnerBotUser innerBotUser;
}
