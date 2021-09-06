package com.scribassu.scribabot.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.OffsetDateTime;

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
    private boolean silentEmptyDays;
    private boolean sentKeyboard;

    @CreatedDate
    private OffsetDateTime registrationTimestamp;

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
                ", silentEmptyDays=" + silentEmptyDays +
                ", sentKeyboard=" + sentKeyboard +
                ", registrationTimestamp=" + registrationTimestamp +
                '}';
    }
}
