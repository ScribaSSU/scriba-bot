package com.scribassu.scribabot.entities;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class ExamPeriodTomorrowNotification {
    @Id
    @GeneratedValue
    private Long id;

    private String userId;
    private boolean isEnabled;
    private Integer hourForSend;

    public ExamPeriodTomorrowNotification(String userId, boolean isEnabled, int hourForSend) {
        this.userId = userId;
        this.isEnabled = isEnabled;
        this.hourForSend = hourForSend;
    }
}