package com.scribassu.scribabot.entities;

import com.scribassu.scribabot.text.CommandText;
import com.scribassu.scribabot.util.BotUserSource;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class BotUser {
    @Id
    private String userId;

    @Enumerated(EnumType.STRING)
    private BotUserSource source;

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

    public boolean wantTeacherSchedule() {
        return previousUserMessage.startsWith(CommandText.TEACHER_ID_PAYLOAD);
    }

    public boolean fromVk() {
        return BotUserSource.VK.equals(source);
    }

    public boolean fromTg() {
        return BotUserSource.TG.equals(source);
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
