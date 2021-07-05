package com.scribassu.scribabot.controllers;

import com.scribassu.scribabot.util.WeekTypeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeekController {

    @Value("${week-type.access-token}")
    private String accessToken;

    @GetMapping("/weekType")
    public ResponseEntity shiftWeekType(@RequestParam("shift") byte shift,
                                        @RequestParam("accessToken") String token) {
        if (!StringUtils.isEmpty(token) && accessToken.equals(token)) {
            if (shift >= 0 && shift <= 1) {
                WeekTypeUtils.SHIFT_WEEK_TYPE = shift;
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
