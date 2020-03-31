package com.scribassu.scribabot.dto.vkkeyboard;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum VkKeyboardButtonColor {
    @JsonProperty("default")
    DEFAULT("default"),
    @JsonProperty("positive")
    POSITIVE("positive"),
    @JsonProperty("negative")
    NEGATIVE("negative"),
    @JsonProperty("primary")
    PRIMARY("primary");

    private final String value;

    VkKeyboardButtonColor(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
