package com.scribassu.scribabot.controllers;

import com.scribassu.scribabot.services.*;
import com.scribassu.scribabot.util.Constants;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

@RestController
public class VkController {

    @Value("${scriba-bot.token}")
    private String token;

    @Value("${scriba-bot.confirmation-code}")
    private String confirmationCode;

    private final Random random = new Random();
    private final MessageParser messageParser;
    private final MessageHandler messageHandler;
    private final SymbolConverter symbolConverter;

    @Autowired
    public VkController(MessageParser messageParser,
                        MessageHandler messageHandler,
                        SymbolConverter symbolConverter) {
        this.messageParser = messageParser;
        this.messageHandler = messageHandler;
        this.symbolConverter = symbolConverter;
    }

    @PostMapping(value = "/testscriba", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public String getMessage(@RequestBody String incomingMessage){
        System.out.println("Get some message");
        Map<String, String> parsedMessage;
        if(incomingMessage != null){
            parsedMessage = messageParser.parseMessage(incomingMessage);

            if(parsedMessage.containsKey(Constants.KEY_MESSAGE)) {
                String userId = parsedMessage.get(Constants.KEY_USER_ID);
                String message = parsedMessage.get(Constants.KEY_MESSAGE);
                Map<String, String> botMessage = messageHandler.getBotMessage(message);
                String vkApiMethod = "https://api.vk.com/method/messages.send?access_token=" + token + "&v=5.100";
                String botMessageUrl = buildVkApiResponse(vkApiMethod, userId, botMessage.get(Constants.KEY_MESSAGE), botMessage.get(Constants.KEY_KEYBOARD));

                System.out.println(botMessageUrl);
                try {
                    HttpClient client = HttpClientBuilder.create().build();
                    HttpGet httpGet = new HttpGet(botMessageUrl);
                    httpGet.addHeader("accept", "application/x-www-form-urlencoded");
                    HttpResponse response = client.execute(httpGet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            throw new IllegalArgumentException("Message was not received!");
        }
        if(parsedMessage.containsKey(Constants.TYPE_CONFIRMATION)) {
            return confirmationCode;
        }
        else {
            return Constants.OK;
        }
    }

    private String buildVkApiResponse(String vkApiMethod, String userId, String message, String keyboard){
        if(keyboard == null || keyboard.isEmpty()) {
            return (vkApiMethod + "&user_id=" + userId + "&message=" + symbolConverter.convertSymbols(message) + "&random_id=" + random.nextInt());
        }
        else {
            return (vkApiMethod + "&user_id=" + userId + "&message=" + symbolConverter.convertSymbols(message) + "&keyboard=" + keyboard + "&random_id=" + random.nextInt());
        }
    }
}
