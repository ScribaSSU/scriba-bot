package com.scribassu.scribabot.entities.notifications;

import com.scribassu.scribabot.model.BotUserSource;

import javax.persistence.Entity;

@Entity
public class ExtramuralEventAfterTomorrowNotification extends AbstractScheduleNotification {

    public ExtramuralEventAfterTomorrowNotification(String userId,
                                                    BotUserSource userSource,
                                                    boolean isEnabled,
                                                    int hourForSend) {
        super(userId, userSource, isEnabled, hourForSend);
    }

    public ExtramuralEventAfterTomorrowNotification() {
        super();
    }
}