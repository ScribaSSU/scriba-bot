package com.scribassu.scribabot.entities;

import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.BotUserSource;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@NoArgsConstructor
public abstract class BotUser {

    @Id
    private String userId;

    private String department;
    private String groupNumber;
    private String educationForm;

    @Column(length = 1000)
    private String previousUserMessage;

    private boolean filterNomDenom;

    public BotUser(String userId) {
        this.userId = userId;
        this.previousUserMessage = "";
    }

    @Override
    public String toString() {
        return "BotUser{" +
                "userId='" + userId + '\'' +
                ", department='" + department + '\'' +
                ", groupNumber='" + groupNumber + '\'' +
                ", educationForm='" + educationForm + '\'' +
                ", previousUserMessage='" + previousUserMessage + '\'' +
                ", filterNomDenom=" + filterNomDenom +
                '}';
    }
}
