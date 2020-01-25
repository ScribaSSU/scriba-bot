package com.scribassu.scribabot.services.bot;

import org.springframework.stereotype.Service;

@Service
public class HelpService {

    private final String HELP = "Чем смогу, помогу";

    public String getHelp() {
        return HELP;
    }
}
