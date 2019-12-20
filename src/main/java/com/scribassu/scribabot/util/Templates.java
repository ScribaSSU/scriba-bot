package com.scribassu.scribabot.util;

import com.scribassu.tracto.domain.FullTimeLesson;
import com.scribassu.tracto.domain.LessonType;

import java.util.List;

public class Templates {
    public String makeTemplate(List<FullTimeLesson> fullTimeLessons)
    {
        fullTimeLessons.sort((o1, o2) -> (int) (o1.getId() - o2.getId()));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Сегодня ").append(fullTimeLessons.get(0).getDay().getWeekDay()).append("\n Неделя - ")
                .append(fullTimeLessons.get(0).getWeekType()).append("\uD83D\uDD4A \n")
                .append("Группа № ").append(fullTimeLessons.get(0).getStudentGroup()).append("\n");
        for (FullTimeLesson fullTimeLesson : fullTimeLessons) {
            stringBuilder.append(fullTimeLesson.getLessonTime().getTimeStart()).append(" - ")
                   .append(fullTimeLesson.getLessonTime().getTimeFinish()).append("\n");
            if (fullTimeLesson.getLessonType().equals(LessonType.LECTURE))
                stringBuilder.append("Лекция \uD83D\uDCD7 \n");
            else if (fullTimeLesson.getLessonType().equals(LessonType.PRACTICE))
                 stringBuilder.append("Практика \uD83D\uDCD8 \n");
            else if (fullTimeLesson.getLessonType().equals(LessonType.LABORATORY))
                 stringBuilder.append("Лабораторная \uD83D\uDCD5 \n");
            stringBuilder.append(fullTimeLesson.getName()).append("\n")
                    .append(fullTimeLesson.getTeacher().getSurname()).append(" ")
                    .append(fullTimeLesson.getTeacher().getName()).append(" ")
                    .append(fullTimeLesson.getTeacher().getPatronymic()).append("\n")
                    .append(fullTimeLesson.getPlace()).append("\n \n");
        }

        return stringBuilder.toString();
    }
}
