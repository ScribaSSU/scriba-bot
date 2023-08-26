package com.scribassu.scribabot.entities.notifications;


import com.scribassu.scribabot.model.BotUserSource;

import javax.persistence.*;

@Entity
public class ExamPeriodTomorrowNotification extends AbstractScheduleNotification {

    public ExamPeriodTomorrowNotification(String userId,
                                          BotUserSource userSource,
                                          boolean isEnabled,
                                          int hourForSend) {
        super(userId, userSource, isEnabled, hourForSend);
    }

    public ExamPeriodTomorrowNotification() {
        super();
    }
}