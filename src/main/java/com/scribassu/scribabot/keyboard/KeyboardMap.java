package com.scribassu.scribabot.keyboard;

import java.util.HashMap;

public class KeyboardMap {
    public static HashMap<KeyboardType, Keyboard> keyboards = new HashMap<>();

    public static Keyboard get(KeyboardType keyboardType) {
        return keyboards.get(keyboardType);
    }
}
