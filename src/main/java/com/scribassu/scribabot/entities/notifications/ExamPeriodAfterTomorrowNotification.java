package com.scribassu.scribabot.entities.notifications;


import com.scribassu.scribabot.model.BotUserSource;

import javax.persistence.*;

@Entity
public class ExamPeriodAfterTomorrowNotification extends AbstractScheduleNotification {

    public ExamPeriodAfterTomorrowNotification(String userId,
                                               BotUserSource userSource,
                                               boolean isEnabled,
                                               int hourForSend) {
        super(userId, userSource, isEnabled, hourForSend);
    }

    public ExamPeriodAfterTomorrowNotification() {
        super();
    }
}