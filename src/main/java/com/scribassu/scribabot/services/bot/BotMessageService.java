package com.scribassu.scribabot.services.bot;

import com.scribassu.scribabot.model.BotMessage;
import com.scribassu.scribabot.model.InnerBotUser;

import java.util.concurrent.CompletableFuture;

public interface BotMessageService {
    CompletableFuture<BotMessage> getBotMessage(String message, InnerBotUser botUser);
}
