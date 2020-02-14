package com.scribassu.scribabot.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class BotUser {
    @Id
    private String userId;

    private String department;
    private String groupNumber;
    private String educationForm;

    @Column(length = 500)
    private String previousUserMessage;

    public BotUser(String userId) {
        this.userId = userId;
    }
}
