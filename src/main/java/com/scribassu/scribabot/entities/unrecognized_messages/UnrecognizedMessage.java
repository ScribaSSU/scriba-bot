package com.scribassu.scribabot.entities.unrecognized_messages;

import com.scribassu.scribabot.model.Command;
import com.scribassu.scribabot.util.BotUserSource;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class UnrecognizedMessage {

    @Id
    @GeneratedValue
    private Long id;

    private String message;
    private String payload;

    private String userId;

    @Enumerated(EnumType.STRING)
    private BotUserSource userSource;

    @CreatedDate
    private OffsetDateTime createdDate;

    public UnrecognizedMessage(Command command, String userId, BotUserSource userSource) {
        this.message = command.getMessage();
        this.payload = command.getPayload();
        this.userId = userId;
        this.userSource = userSource;
    }
}
