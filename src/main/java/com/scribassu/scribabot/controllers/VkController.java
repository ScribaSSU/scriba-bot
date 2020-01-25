package com.scribassu.scribabot.controllers;

import com.scribassu.scribabot.services.*;
import com.scribassu.scribabot.services.messages.MessageHandler;
import com.scribassu.scribabot.services.messages.MessageParser;
import com.scribassu.scribabot.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    public VkController(MessageParser messageParser,
                        MessageHandler messageHandler) {
        this.messageParser = messageParser;
        this.messageHandler = messageHandler;
    }

    @PostMapping(value = "/testscriba", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public String getMessage(@RequestBody String incomingMessage) throws Exception {
        System.out.println("Get some message");
        Map<String, String> parsedMessage;
        if(incomingMessage != null){
            parsedMessage = messageParser.parseMessage(incomingMessage);

            if(parsedMessage.containsKey(Constants.KEY_MESSAGE)) {
                String userId = parsedMessage.get(Constants.KEY_USER_ID);
                String message = parsedMessage.get(Constants.KEY_MESSAGE);
                Map<String, String> botMessage = messageHandler.getBotMessage(message, userId);

                String vkApiMethod = "https://api.vk.com/method/messages.send";

                List<NameValuePair> postParameters = new ArrayList<>();
                postParameters.add(new BasicNameValuePair("access_token", token));
                postParameters.add(new BasicNameValuePair("v", "5.100"));
                postParameters.add(new BasicNameValuePair("user_id", userId));
                postParameters.add(new BasicNameValuePair("random_id", String.valueOf(random.nextInt())));
                postParameters.add(new BasicNameValuePair("message", botMessage.get(Constants.KEY_MESSAGE)));

                if(!StringUtils.isEmpty(botMessage.get(Constants.KEY_KEYBOARD))) {
                    postParameters.add(new BasicNameValuePair("keyboard", botMessage.get(Constants.KEY_KEYBOARD)));
                }

                HttpPost postRequest = new HttpPost(vkApiMethod);
                postRequest.addHeader("accept", "application/x-www-form-urlencoded");
                postRequest.setEntity(new UrlEncodedFormEntity(postParameters, StandardCharsets.UTF_8));

                HttpClient client = HttpClientBuilder.create().build();
                HttpResponse response = client.execute(postRequest);

                System.out.println("RESPONSE: " + response);
                System.out.println(response.getEntity().getContent());
                System.out.println("END OF RESPONSE");
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
}
