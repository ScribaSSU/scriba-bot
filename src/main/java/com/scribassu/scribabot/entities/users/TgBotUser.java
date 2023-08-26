package com.scribassu.scribabot.entities.users;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class TgBotUser extends BotUser {

    public TgBotUser(String userId) {
        super(userId);
    }
}
