package com.scribassu.scribabot.util;

import com.scribassu.scribabot.text.CommandText;
import com.scribassu.tracto.domain.FullTimeLesson;
import com.scribassu.tracto.domain.LessonType;
import com.scribassu.tracto.domain.WeekType;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Templates {

    public static String makeTemplate(List<FullTimeLesson> fullTimeLessons,
                                      String day,
                                      String dayNumber)
    {
        StringBuilder stringBuilder = new StringBuilder();
        Calendar calendar = CalendarUtils.getCalendar();

        if(day.equalsIgnoreCase(CommandText.TODAY)) {
            stringBuilder.append("Сегодня ");
        }
        if(day.equalsIgnoreCase(CommandText.TOMORROW)) {
            stringBuilder.append("Завтра ");
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        if(day.equalsIgnoreCase(CommandText.YESTERDAY)) {
            stringBuilder.append("Вчера ");
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        stringBuilder.append(BotMessageUtils.getDayByDayNumber(dayNumber)).append(" ");

        String data = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);

        if(day.equalsIgnoreCase(CommandText.TODAY)
                || day.equalsIgnoreCase(CommandText.TOMORROW)
                || day.equalsIgnoreCase(CommandText.YESTERDAY)) {
            stringBuilder.append(data);
        }
        stringBuilder.append("\n");
        //stringBuilder.append("Сегодня ").append(fullTimeLessons.get(0).getDay().getWeekDay().getDay()).append("\nНеделя - ");
        //stringBuilder.append(fullTimeLessons.get(0).getWeekType().getType()).append("\uD83D\uDD4A\n");
        if(CollectionUtils.isEmpty(fullTimeLessons)) {
            stringBuilder.append("А пар-то нету :)");
        }
        else {
            fullTimeLessons.sort((o1, o2) -> (o1.getLessonTime().getLessonNumber() - o2.getLessonTime().getLessonNumber()));
            stringBuilder.append("Группа № ").append(fullTimeLessons.get(0).getStudentGroup().getGroupNumber()).append("\n \n");
            for (FullTimeLesson fullTimeLesson : fullTimeLessons) {
                stringBuilder.append(fullTimeLesson.getLessonTime().getTimeStart()).append(" - ")
                        .append(fullTimeLesson.getLessonTime().getTimeFinish()).append("\n")
                        .append(fullTimeLesson.getLessonType().getType());
                if (fullTimeLesson.getLessonType().equals(LessonType.LECTURE))
                    stringBuilder.append(" \uD83D\uDCD7\n");
                else if (fullTimeLesson.getLessonType().equals(LessonType.PRACTICE))
                    stringBuilder.append(" \uD83D\uDCD8\n");
                else if (fullTimeLesson.getLessonType().equals(LessonType.LABORATORY))
                    stringBuilder.append(" \uD83D\uDCD5\n");
                if(!fullTimeLesson.getWeekType().equals(WeekType.FULL)) {
                    stringBuilder.append(fullTimeLesson.getWeekType().getType()).append("\n");
                }
                if (!fullTimeLesson.getSubGroup().isEmpty())
                    stringBuilder.append(fullTimeLesson.getSubGroup().trim()).append("\n");
                stringBuilder.append(fullTimeLesson.getName()).append("\n")
                        .append(fullTimeLesson.getTeacher().getSurname()).append(" ")
                        .append(fullTimeLesson.getTeacher().getName()).append(" ")
                        .append(fullTimeLesson.getTeacher().getPatronymic()).append("\n")
                        .append(fullTimeLesson.getPlace()).append("\n \n");
            }
        }

        return stringBuilder.toString();
    }
}
