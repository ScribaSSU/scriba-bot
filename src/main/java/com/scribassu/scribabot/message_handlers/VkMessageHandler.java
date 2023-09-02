package com.scribassu.scribabot.message_handlers;

import com.scribassu.scribabot.dto.vk.VkMessageDto;
import com.scribassu.scribabot.mapper.HttpMapper;
import com.scribassu.scribabot.text.MessageText;
import com.scribassu.scribabot.util.Constants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@Data
@Slf4j
public class VkMessageHandler {

    @Value("${scriba-bot.vk.confirmation-code}")
    private String confirmationCode;

    private final VkMessageSender vkMessageSender;
    private final MessageHandler messageHandler;

    private final HttpMapper httpMapper;

    @PostMapping(value = "${scriba-bot.vk.url}", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public String getMessage(@RequestBody VkMessageDto incomingMessage) throws Exception {
        log.info("VK message: {}", incomingMessage);
        if (incomingMessage.isConfirmation()) {
            return confirmationCode;
        }

        var command = httpMapper.toCommand(incomingMessage);
        messageHandler.getBotMessage(command).thenApply(message -> {
            //If somebody writes command without bot name mention in chat, bot should keep the silence
            if (!message.getMessage().equalsIgnoreCase(MessageText.DO_NOT_SEND)) {
                vkMessageSender.send(message);
            }
            return CompletableFuture.completedFuture(message);
        });

        //If somebody writes command without bot name mention in chat, bot should keep the silence
//        if (!botMessage.getMessage().equalsIgnoreCase(MessageText.DO_NOT_SEND)) {
//               vkMessageSender.send(botMessage, command.getUserId());
//        }

        return Constants.OK;
    }
}
