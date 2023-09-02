package com.scribassu.scribabot.entities.notifications;

import com.scribassu.scribabot.model.BotUserSource;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class ScheduleNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private BotUserSource userSource;

    private boolean isEnabled;
    private Integer hourForSend;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    public ScheduleNotification(String userId,
                                BotUserSource userSource,
                                boolean isEnabled,
                                int hourForSend,
                                NotificationType notificationType) {
        this.userId = userId;
        this.userSource = userSource;
        this.isEnabled = isEnabled;
        this.hourForSend = hourForSend;
        this.notificationType = notificationType;
    }

    public boolean fromVk() {
        return BotUserSource.VK.equals(this.userSource);
    }
}
