package com.scribassu.scribabot.dto.rest;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

// TODO remove after tracto-domain version update
@Data
@NoArgsConstructor
public class WeekShift {
    private Long id;
    private int shift;
    private String department;

    public WeekShift(int shift, String department) {
        this.shift = shift;
        this.department = department;
    }
}
