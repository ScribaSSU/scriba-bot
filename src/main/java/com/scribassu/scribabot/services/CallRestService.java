package com.scribassu.scribabot.services;

import com.scribassu.scribabot.dto.FullTimeLessonDto;
import com.scribassu.scribabot.dto.GroupNumbersDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CallRestService {

    @Value("${call.rest.prefix}")
    private String prefix;

    private String FULL_DEP_EDU_GROUP_URI = "schedule/full/%s/%s";
    private String FULL_DEP_EDU_GROUP_DAY_URI = "schedule/full/%s/%s/%s";
    private String FULL_DEP_EDU_GROUP_DAY_LESSON_URI = "schedule/full/%s/%s/%s/%s";

    private String STUDENT_GROUP_NUMBER_URI = "group/number/%s/%s/%s";

    public FullTimeLessonDto getFullTimeLessonsByDayAndLesson(String departmentUrl,
                                                              String groupNumber,
                                                              String dayNumber,
                                                              String lessonNumber) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = prefix +
                String.format(
                        FULL_DEP_EDU_GROUP_DAY_LESSON_URI,
                        departmentUrl,
                        groupNumber,
                        dayNumber,
                        lessonNumber
                );

        //https://stackoverflow.com/questions/19540289/how-to-fix-the-java-security-cert-certificateexception-no-subject-alternative
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);

        return restTemplate.getForObject(uri, FullTimeLessonDto.class);
    }

    public FullTimeLessonDto getFullTimeLessonsByDay(String departmentUrl,
                                                     String groupNumber,
                                                     String dayNumber) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = prefix +
                String.format(
                        FULL_DEP_EDU_GROUP_DAY_URI,
                        departmentUrl,
                        groupNumber,
                        dayNumber
                );

        //https://stackoverflow.com/questions/19540289/how-to-fix-the-java-security-cert-certificateexception-no-subject-alternative
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);

        return restTemplate.getForObject(uri, FullTimeLessonDto.class);
    }

    public FullTimeLessonDto getFullTimeLessonsByGroup(String departmentUrl,
                                                       String groupNumber) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = prefix +
                String.format(
                        FULL_DEP_EDU_GROUP_URI,
                        departmentUrl,
                        groupNumber
                );

        //https://stackoverflow.com/questions/19540289/how-to-fix-the-java-security-cert-certificateexception-no-subject-alternative
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);

        return restTemplate.getForObject(uri, FullTimeLessonDto.class);
    }

    public GroupNumbersDto getGroupNumbersByDepartmentUrlAndEducationFormAndCourse(
            String departmentUrl,
            String educationForm,
            String course) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = prefix +
                String.format(
                        STUDENT_GROUP_NUMBER_URI,
                        departmentUrl,
                        educationForm,
                        course
                );

        //https://stackoverflow.com/questions/19540289/how-to-fix-the-java-security-cert-certificateexception-no-subject-alternative
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);

        return restTemplate.getForObject(uri, GroupNumbersDto.class);
    }
}
