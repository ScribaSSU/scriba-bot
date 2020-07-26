package com.scribassu.scribabot.dto.rest;

import com.scribassu.tracto.domain.ExamPeriodEvent;
import com.scribassu.tracto.domain.Teacher;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TeacherExamPeriodEventDto {

    private List<ExamPeriodEvent> examPeriodEvents;
    private Teacher teacher;

    public TeacherExamPeriodEventDto(List<ExamPeriodEvent> examPeriodEvents,
                                     Teacher teacher) {
        this.examPeriodEvents = examPeriodEvents;
        this.teacher = teacher;
    }
}
