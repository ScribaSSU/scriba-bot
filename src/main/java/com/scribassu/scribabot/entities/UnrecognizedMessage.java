package com.scribassu.scribabot.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class UnrecognizedMessage {

    @Id
    @GeneratedValue
    private Long id;

    private String command;
    private String botUser;

    public UnrecognizedMessage(String command, String botUser) {
        this.command = command;
        this.botUser = botUser;
    }
}
