package com.scribassu.scribabot.dto.vkkeyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VkKeyboardButton {
    @JsonProperty("action")
    private VkKeyboardButtonActionText action;
    @JsonProperty("color")
    private VkKeyboardButtonColor color;

    public VkKeyboardButton(VkKeyboardButtonActionText action,
                            VkKeyboardButtonColor color) {
        this.action = action;
        this.color = color;
    }
}
