package com.scribassu.scribabot.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class BotUser {
    @Id
    public String userId;
    public String department;
    public String groupNumber;
    public String educationForm;

    public BotUser(String userId) {
        this.userId = userId;
    }
}
