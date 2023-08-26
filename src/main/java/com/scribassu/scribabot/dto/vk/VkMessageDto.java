package com.scribassu.scribabot.dto.vk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class VkMessageDto {
    @JsonProperty("group_id")
    private long groupId;

    @JsonProperty("type")
    private VkMessageType type;

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("v")
    private String version;

    @JsonProperty("object")
    private VkMessageObject object;

    @JsonProperty("secret")
    private String secret;

    public boolean isConfirmation() {
        return this.type.equals(VkMessageType.confirmation);
    }

    public boolean isNewMessage() {
        return this.type.equals(VkMessageType.message_new);
    }
}