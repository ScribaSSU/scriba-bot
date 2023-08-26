package com.scribassu.scribabot.entities.notifications;

import com.scribassu.scribabot.util.BotUserSource;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ExtramuralEventTodayNotification extends AbstractScheduleNotification {

    public ExtramuralEventTodayNotification(String userId,
                                            BotUserSource userSource,
                                            boolean isEnabled,
                                            int hourForSend) {
        super(userId, userSource, isEnabled, hourForSend);
    }

    public ExtramuralEventTodayNotification() {
        super();
    }
}
