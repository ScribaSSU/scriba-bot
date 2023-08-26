package com.scribassu.scribabot.entities.users;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class VkBotUser extends BotUser {

    public VkBotUser(String userId) {
        super(userId);
    }
}
