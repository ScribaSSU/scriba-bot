package com.scribassu.scribabot.dto.vkkeyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class VkKeyboard {
    @JsonProperty("buttons")
    private List<List<VkKeyboardButton>> buttons;
    @JsonProperty("one_time")
    private Boolean oneTime;

    public VkKeyboard(List<List<VkKeyboardButton>> buttons, Boolean oneTime) {
        this.buttons = buttons;
        this.oneTime = oneTime;
    }
}
