package com.scribassu.scribabot.entities;

import com.scribassu.tracto.domain.Department;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class WeekShift {
    @Id
    @GeneratedValue
    private Long id;
    private byte shift;
    private String department;
}
