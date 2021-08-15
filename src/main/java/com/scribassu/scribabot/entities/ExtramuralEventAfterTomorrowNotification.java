package com.scribassu.scribabot.entities;

import com.scribassu.scribabot.util.BotUserSource;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class ExtramuralEventAfterTomorrowNotification {
    @Id
    @GeneratedValue
    private Long id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private BotUserSource userSource;

    private boolean isEnabled;
    private Integer hourForSend;

    public ExtramuralEventAfterTomorrowNotification(String userId,
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