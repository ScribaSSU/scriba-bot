package com.scribassu.scribabot.entities.notifications;

import com.scribassu.scribabot.model.BotUserSource;

import javax.persistence.Entity;

@Entity
public class ExtramuralEventTomorrowNotification extends AbstractScheduleNotification {

    public ExtramuralEventTomorrowNotification(String userId,
                                               BotUserSource userSource,
                                               boolean isEnabled,
                                               int hourForSend) {
        super(userId, userSource, isEnabled, hourForSend);
    }

    public ExtramuralEventTomorrowNotification() {
        super();
    }
}
