package com.scribassu.scribabot.services.messages;

import com.scribassu.scribabot.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class MessageSenderImpl implements MessageSender {

    private static final String VK_API_METHOD = "https://api.vk.com/method/messages.send";
    private static final Random random = new Random();

    @Value("${scriba-bot.token}")
    private String token;

    @Value("${scriba-bot.vk-api-version}")
    private String vkApiVersion;

    public void send(Map<String, String> botMessage, String userId) throws Exception {
        List<NameValuePair> postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("access_token", token));
        postParameters.add(new BasicNameValuePair("v", vkApiVersion));
        postParameters.add(new BasicNameValuePair("user_id", userId));
        postParameters.add(new BasicNameValuePair("random_id", String.valueOf(random.nextInt())));
        postParameters.add(new BasicNameValuePair("message", botMessage.get(Constants.KEY_MESSAGE)));

        if(!StringUtils.isEmpty(botMessage.get(Constants.KEY_KEYBOARD))) {
            postParameters.add(new BasicNameValuePair("keyboard", botMessage.get(Constants.KEY_KEYBOARD)));
        }

        HttpPost postRequest = new HttpPost(VK_API_METHOD);
        postRequest.addHeader("accept", "application/x-www-form-urlencoded");
        postRequest.setEntity(new UrlEncodedFormEntity(postParameters, StandardCharsets.UTF_8));

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(postRequest);

        System.out.println("RESPONSE: " + response);
        System.out.println(response.getEntity().getContent());
        System.out.println("END OF RESPONSE");
    }
}
