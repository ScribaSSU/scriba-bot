package com.scribassu.scribabot.entities.notifications;

import com.scribassu.scribabot.util.BotUserSource;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ExamPeriodTodayNotification extends AbstractScheduleNotification {

    public ExamPeriodTodayNotification(String userId,
                                       BotUserSource userSource,
                                       boolean isEnabled,
                                       int hourForSend) {
        super(userId, userSource, isEnabled, hourForSend);
    }

    public ExamPeriodTodayNotification() {
        super();
    }
}
