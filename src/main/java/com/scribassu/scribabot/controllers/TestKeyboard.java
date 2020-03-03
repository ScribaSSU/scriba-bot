package com.scribassu.scribabot.controllers;

import com.scribassu.scribabot.services.messages.TestKeyboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestKeyboard {

    @GetMapping("/test")
    public void k() throws Exception{
        System.out.println(TestKeyboardService.getTestKeyboard());
    }
}
