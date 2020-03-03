package com.scribassu.scribabot.dto.vkkeyboard;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum VkKeyboardButtonActionType {
    @JsonProperty("text")
    TEXT("text");

    private final String value;

    VkKeyboardButtonActionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}