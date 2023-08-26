package com.scribassu.scribabot.model.keyboard;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class KeyboardButton {
    private String text;
    private Optional<String> payload;

    public KeyboardButton(String text) {
        this.text = text;
        this.payload = Optional.empty();
    }
}
