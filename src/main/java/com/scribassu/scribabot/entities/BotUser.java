package com.scribassu.scribabot.entities;

import com.scribassu.scribabot.text.CommandText;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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
