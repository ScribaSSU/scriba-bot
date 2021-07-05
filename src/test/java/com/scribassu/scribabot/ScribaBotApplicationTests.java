package com.scribassu.scribabot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = {"dev"})
public class ScribaBotApplicationTests {

    @Test
    public void contextLoads() {
    }
}