package com.scribassu.scribabot.entities.users;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.OffsetDateTime;

@MappedSuperclass
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
        this.sentKeyboard = true;
        this.silentEmptyDays = false;
        this.filterNomDenom = false;
    }
}
