package com.scribassu.scribabot.controllers;

import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.dto.Command;
import com.scribassu.scribabot.services.messages.MessageHandler;
import com.scribassu.scribabot.services.messages.MessageParser;
import com.scribassu.scribabot.services.messages.VkMessageSender;
import com.scribassu.scribabot.text.MessageText;
import com.scribassu.scribabot.util.BotUserSource;
import com.scribassu.scribabot.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class VkController {

    @Value("${scriba-bot.vk.confirmation-code}")
    private String confirmationCode;

    private final MessageParser messageParser;
    private final VkMessageSender vkMessageSender;
    private final MessageHandler messageHandler;

    @Autowired
    public VkController(MessageParser messageParser,
                        VkMessageSender vkMessageSender,
                        MessageHandler messageHandler) {
        this.messageParser = messageParser;
        this.vkMessageSender = vkMessageSender;
        this.messageHandler = messageHandler;
    }

    @PostMapping(value = "${scriba-bot.vk.url}", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public String getMessage(@RequestBody String incomingMessage) throws Exception {
        Map<String, String> parsedMessage;
        if (StringUtils.isNotEmpty(incomingMessage)) {
            parsedMessage = messageParser.parseMessage(incomingMessage);

            if (parsedMessage.containsKey(Constants.KEY_MESSAGE)) {
                String userId = parsedMessage.get(Constants.KEY_USER_ID);
                Command command = new Command(
                        parsedMessage.get(Constants.KEY_MESSAGE),
                        parsedMessage.getOrDefault(Constants.KEY_PAYLOAD, ""),
                        userId,
                        BotUserSource.VK);
                BotMessage botMessage = messageHandler.getBotMessage(command);
                //If somebody writes command without bot name mention in chat, bot should keep the silence
                if (!botMessage.getMessage().equalsIgnoreCase(MessageText.DO_NOT_SEND)) {
                    vkMessageSender.send(botMessage, userId);
                }
            }
        } else {
            throw new IllegalArgumentException("Message was not received!");
        }
        if (parsedMessage.containsKey(Constants.TYPE_CONFIRMATION)) {
            return confirmationCode;
        } else {
            return Constants.OK;
        }
    }
}
