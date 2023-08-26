package com.scribassu.scribabot.dto.vk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.scribassu.scribabot.config.VkAttachmentDeserializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class VkMessageObject {
    @JsonProperty("date")
    private long date;
    @JsonProperty("from_id")
    private long fromId;
    @JsonProperty("id")
    private long id;
    @JsonProperty("out")
    private long out;
    @JsonProperty("attachments")
    @JsonDeserialize(using = VkAttachmentDeserializer.class)
    private List<VkAttachment> attachments;
    @JsonProperty("conversation_message_id")
    private long conversationMessageId;
    @JsonProperty("fwd_messages")
    private List<Object> forwardedMessages;
    @JsonProperty("important")
    private boolean important;
    @JsonProperty("is_hidden")
    private boolean isHidden;
    @JsonProperty("payload")
    private String payload;
    @JsonProperty("peer_id")
    private long peerId;
    @JsonProperty("random_id")
    private long randomId;
    @JsonProperty("text")
    private String text;
}
