package com.scribassu.scribabot.model.inner_keyboard;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class InnerKeyboardButton {
    private String text;
    private Optional<String> payload;

    public InnerKeyboardButton(String text) {
        this.text = text;
        this.payload = Optional.empty();
    }
}
