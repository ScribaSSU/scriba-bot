package com.scribassu.scribabot.model.inner_keyboard;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InnerKeyboard {
    private List<List<InnerKeyboardButton>> buttons;

    public InnerKeyboard() {
        this.buttons = new ArrayList<>();
    }

    public boolean add(List<InnerKeyboardButton> buttons) {
        return this.buttons.add(buttons);
    }

    public List<InnerKeyboardButton> get(int index) {
        return this.buttons.get(index);
    }

    public void remove(int index) {
        this.buttons.remove(index);
    }
}
