package com.scribassu.scribabot.keyboard;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Keyboard {

    private String filename;
    private String jsonText;

    public Keyboard(String filename, String jsonText) {
        this.filename = filename;
        this.jsonText = jsonText;
    }
}
