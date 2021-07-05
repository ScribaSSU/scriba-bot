package com.scribassu.scribabot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ScribaBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScribaBotApplication.class, args);
    }

}
