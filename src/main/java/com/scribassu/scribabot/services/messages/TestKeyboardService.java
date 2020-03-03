package com.scribassu.scribabot.services.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scribassu.scribabot.dto.vkkeyboard.*;

import java.util.ArrayList;
import java.util.List;

public class TestKeyboardService {

    public static String getTestKeyboard() throws Exception {
        VkKeyboard k = new VkKeyboard();
        k.setOneTime(true);
        List<List<VkKeyboardButton>> b = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            b.add(new ArrayList<>());
            for(int j = 0; j < 4; j++) {
                VkKeyboardButton kb = new VkKeyboardButton();
                VkKeyboardButtonActionText kba = new VkKeyboardButtonActionText();
                kba.setType(VkKeyboardButtonActionType.TEXT);
                kba.setLabel("k" + i + j);
                kba.setPayload("p" + i + j);
                kb.setAction(kba);
                kb.setColor(VkKeyboardButtonColor.POSITIVE);
                b.get(i).add(kb);
                k.setButtons(b);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(k);

        return s;
    }
}
