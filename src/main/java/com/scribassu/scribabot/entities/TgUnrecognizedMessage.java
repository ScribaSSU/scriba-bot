package com.scribassu.scribabot.entities;

import com.scribassu.scribabot.dto.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.OffsetDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TgUnrecognizedMessage extends Command {

    @Id
    @GeneratedValue
    private Long id;

    private String message;
    private String payload;

    @ManyToOne
    private TgBotUser botUser;

    @CreatedDate
    private OffsetDateTime createdDate;

    public TgUnrecognizedMessage(Command command, TgBotUser botUser) {
        this.message = command.getMessage();
        this.payload = command.getPayload();
        this.botUser = botUser;
    }

    public TgUnrecognizedMessage(String message, String payload, TgBotUser botUser) {
        this.message = message;
        this.payload = payload;
        this.botUser = botUser;
    }
}
