package com.scribassu.scribabot.entities.notifications;

import com.scribassu.scribabot.util.BotUserSource;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractScheduleNotification {

    @Id
    @GeneratedValue
    private Long id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private BotUserSource userSource;

    private boolean isEnabled;
    private Integer hourForSend;

    public AbstractScheduleNotification(String userId,
                                        BotUserSource userSource,
                                        boolean isEnabled,
                                        int hourForSend) {
        this.userId = userId;
        this.userSource = userSource;
        this.isEnabled = isEnabled;
        this.hourForSend = hourForSend;
    }

    public boolean fromVk() {
        return BotUserSource.VK.equals(this.userSource);
    }
}
