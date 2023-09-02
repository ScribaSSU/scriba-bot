package com.scribassu.scribabot.message_handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scribassu.scribabot.generators.InnerKeyboardGenerator;
import com.scribassu.scribabot.mapper.HttpMapper;
import com.scribassu.scribabot.model.BotMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.scribassu.scribabot.util.Constants.EMPTY_VK_KEYBOARD;

@Service
@Slf4j
@Data
public class VkMessageSender implements MessageSender {

    private final HttpMapper httpMapper;
    private final InnerKeyboardGenerator innerKeyboardGenerator;

    private static final Integer VK_LENGTH = 4096;
    private static final String VK_API_METHOD = "https://api.vk.com/method/messages.send";
    private static final Random random = new Random();

    @Value("${scriba-bot.vk.token}")
    private String token;

    @Value("${scriba-bot.vk.api-version}")
    private String vkApiVersion;

    @Async
    @Override
    public void send(BotMessage botMessage) {
        var userId = botMessage.getBotUser().getUserId();
        try {
            var startIndex = 0;
            String message;
            var lastSpaceIndex = 0;
            var inputMessage = botMessage.getMessage();
            while (startIndex < inputMessage.length()) {
                if (inputMessage.length() - startIndex <= VK_LENGTH) {
                    message = inputMessage.substring(startIndex);
                } else {
                    lastSpaceIndex = inputMessage.lastIndexOf(' ', startIndex + VK_LENGTH);
                    message = inputMessage.substring(startIndex, lastSpaceIndex);
                }
                List<NameValuePair> postParameters = new ArrayList<>();
                postParameters.add(new BasicNameValuePair("access_token", token));
                postParameters.add(new BasicNameValuePair("v", vkApiVersion));
                postParameters.add(new BasicNameValuePair("peer_id", userId));
                postParameters.add(new BasicNameValuePair("random_id", String.valueOf(random.nextInt())));
                postParameters.add(new BasicNameValuePair("message", message));

                if (botMessage.getBotUser().isSentKeyboard() && botMessage.hasKeyboard()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String keyboard;
                    try {
                        keyboard = objectMapper.writeValueAsString(httpMapper.toVkKeyboard(botMessage.getInnerKeyboard().get()));
                    } catch (JsonProcessingException e) {
                        keyboard = objectMapper.writeValueAsString(httpMapper.toVkKeyboard(innerKeyboardGenerator.mainMenu(botMessage.getBotUser())));
                    }
                    postParameters.add(new BasicNameValuePair("keyboard", keyboard));
                } else {
                    postParameters.add(new BasicNameValuePair("keyboard", EMPTY_VK_KEYBOARD));
                }

                HttpPost postRequest = new HttpPost(VK_API_METHOD);
                postRequest.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
                postRequest.setEntity(new UrlEncodedFormEntity(postParameters, StandardCharsets.UTF_8));
                HttpClient client = HttpClientBuilder.create().build();
                HttpResponse response = client.execute(postRequest);

                if (lastSpaceIndex > 0) {
                    startIndex = lastSpaceIndex + 1;
                    lastSpaceIndex = 0;
                } else {
                    startIndex += VK_LENGTH;
                }
            }
        } catch (Exception e) {
            log.error(String.format("Failed to send VK message to user %s: %s", userId, e.getMessage()));
        }
    }
}
