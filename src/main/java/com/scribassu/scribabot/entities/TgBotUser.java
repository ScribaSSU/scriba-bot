package com.scribassu.scribabot.entities;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class TgBotUser extends BotUser {

    public TgBotUser(String userId) {
        super(userId);
    }
}
