package com.scribassu.scribabot.services.startup;

import com.scribassu.scribabot.keyboard.Keyboard;
import com.scribassu.scribabot.keyboard.KeyboardMap;
import com.scribassu.scribabot.keyboard.KeyboardType;
import com.scribassu.scribabot.services.ChangeKeyboardSymbols;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class StartupEvents implements ApplicationListener<ContextRefreshedEvent> {

    private final ChangeKeyboardSymbols changeKeyboardSymbols;

    @Value("${scriba-bot.keyboards-folder}")
    private String keyboardsFolder;

    @Autowired
    public StartupEvents(ChangeKeyboardSymbols changeKeyboardSymbols) {
        this.changeKeyboardSymbols = changeKeyboardSymbols;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        for(KeyboardType k : KeyboardType.values()) {
            KeyboardMap.keyboards.put(
                    k,
                    new Keyboard(
                            k.toString(),
                            changeKeyboardSymbols.changeSymbols(keyboardsFolder + k.getFilename())
                    )
            );
        }
    }
}
