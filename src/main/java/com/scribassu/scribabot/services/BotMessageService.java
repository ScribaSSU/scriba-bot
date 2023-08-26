package com.scribassu.scribabot.services;

import com.scribassu.scribabot.model.BotMessage;
import com.scribassu.scribabot.model.BotUser;

import java.util.concurrent.CompletableFuture;

public interface BotMessageService {
    CompletableFuture<BotMessage> getBotMessage(String message, BotUser botUser);
}
