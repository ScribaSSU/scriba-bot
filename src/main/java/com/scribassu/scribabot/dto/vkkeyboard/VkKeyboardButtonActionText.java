package com.scribassu.scribabot.dto.vkkeyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VkKeyboardButtonActionText {

    @JsonProperty("label")
    private String label;
    @JsonProperty("payload")
    private String payload;
    @JsonProperty("type")
    private VkKeyboardButtonActionType type;

    public VkKeyboardButtonActionText(String label,
                                      String payload,
                                      VkKeyboardButtonActionType type) {
        this.label = label;
        this.payload = payload;
        this.type = type;
    }
}
