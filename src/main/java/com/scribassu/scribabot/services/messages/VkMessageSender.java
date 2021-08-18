package com.scribassu.scribabot.services.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scribassu.scribabot.dto.BotMessage;
import com.scribassu.scribabot.generators.VkKeyboardGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class VkMessageSender implements MessageSender {

    private static final Integer VK_LENGTH = 4096;
    private static final String VK_API_METHOD = "https://api.vk.com/method/messages.send";
    private static final Random random = new Random();

    @Value("${scriba-bot.vk.token}")
    private String token;

    @Value("${scriba-bot.vk.api-version}")
    private String vkApiVersion;

    @Override
    public void send(BotMessage botMessage, String userId) throws Exception {
        int startIndex = 0;
        String message;
        int lastSpaceIndex = 0;
        while (startIndex < botMessage.getMessage().length()) {
            if (botMessage.getMessage().length() - startIndex <= VK_LENGTH) {
                message = botMessage.getMessage().substring(startIndex);
            } else {
                lastSpaceIndex = botMessage.getMessage().lastIndexOf(' ', startIndex + VK_LENGTH);
                message = botMessage.getMessage().substring(startIndex, lastSpaceIndex);
            }
            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("access_token", token));
            postParameters.add(new BasicNameValuePair("v", vkApiVersion));
            postParameters.add(new BasicNameValuePair("peer_id", userId));
            postParameters.add(new BasicNameValuePair("random_id", String.valueOf(random.nextInt())));
            postParameters.add(new BasicNameValuePair("message", message));

            if (botMessage.hasVkKeyboard()) {
                ObjectMapper objectMapper = new ObjectMapper();
                String keyboard;
                try {
                    keyboard = objectMapper.writeValueAsString(botMessage.getVkKeyboard());
                } catch (JsonProcessingException e) {
                    keyboard = objectMapper.writeValueAsString(VkKeyboardGenerator.mainMenu);
                }
                postParameters.add(new BasicNameValuePair("keyboard", keyboard));
            }

            HttpPost postRequest = new HttpPost(VK_API_METHOD);
            postRequest.addHeader("accept", "application/x-www-form-urlencoded");
            postRequest.setEntity(new UrlEncodedFormEntity(postParameters, StandardCharsets.UTF_8));
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(postRequest);

            if (lastSpaceIndex > 0) {
                startIndex = lastSpaceIndex + 1;
                lastSpaceIndex = 0;
            } else {
                startIndex += VK_LENGTH;
            }
            log.info("Response: " + response);
        }
    }
}
