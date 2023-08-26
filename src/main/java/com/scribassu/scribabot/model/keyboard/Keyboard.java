package com.scribassu.scribabot.model.keyboard;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Keyboard {
    private List<List<KeyboardButton>> buttons;

    public Keyboard() {
        this.buttons = new ArrayList<>();
    }

    public boolean add(List<KeyboardButton> buttons) {
        return this.buttons.add(buttons);
    }

    public List<KeyboardButton> get(int index) {
        return this.buttons.get(index);
    }

    public void remove(int index) {
        this.buttons.remove(index);
    }
}
