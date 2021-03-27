package com.scribassu.scribabot.services;

import com.scribassu.scribabot.dto.rest.*;
import org.apache.commons.codec.Charsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CallRestService {

    @Value("${call.rest.prefix}")
    private String prefix;

    private String FULL_DEP_EDU_GROUP_URI = "schedule/full/%s/%s";
    private String FULL_DEP_EDU_GROUP_DAY_URI = "schedule/full/%s/%s/%s";
    private String FULL_DEP_EDU_GROUP_DAY_LESSON_URI = "schedule/full/%s/%s/%s/%s";

    private String FULL_EXAM_DEP_GROUP_URI = "exam/full/%s/%s";
    private String FULL_EXAM_DEP_GROUP_DAY_URI = "exam/full/%s/%s/%d/%d";

    private String STUDENT_GROUP_NUMBER_URI = "group/number/%s/%s/%s";

    private String TEACHER_URI = "teacher";

    private String EXTR_EV_GROUP_URI = "schedule/extramural/%s/%s";
    private String EXTR_EV_GROUP_URI_MONTH_DAY = "schedule/extramural/%s/%s/%s/%s";

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

    public ExamPeriodEventDto getFullTimeExamPeriodEvent(String departmentUrl,
                                                         String groupNumber) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = prefix +
                String.format(
                        FULL_EXAM_DEP_GROUP_URI,
                        departmentUrl,
                        groupNumber
                );

        //https://stackoverflow.com/questions/19540289/how-to-fix-the-java-security-cert-certificateexception-no-subject-alternative
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);

        return restTemplate.getForObject(uri, ExamPeriodEventDto.class);
    }

    public ExamPeriodEventDto getFullTimeExamPeriodEventByDay(String departmentUrl,
                                                              String groupNumber,
                                                              Integer month,
                                                              Integer day) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = prefix +
                String.format(
                        FULL_EXAM_DEP_GROUP_DAY_URI,
                        departmentUrl,
                        groupNumber,
                        month,
                        day
                );

        //https://stackoverflow.com/questions/19540289/how-to-fix-the-java-security-cert-certificateexception-no-subject-alternative
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);

        return restTemplate.getForObject(uri, ExamPeriodEventDto.class);
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

    public GroupNumbersDto getOtherGroupNumbersByDepartmentUrlAndEducationForm(
            String departmentUrl,
            String educationForm) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = prefix +
                String.format(
                        STUDENT_GROUP_NUMBER_URI,
                        departmentUrl,
                        educationForm,
                        "other"
                );

        //https://stackoverflow.com/questions/19540289/how-to-fix-the-java-security-cert-certificateexception-no-subject-alternative
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);

        return restTemplate.getForObject(uri, GroupNumbersDto.class);
    }

    public TeacherListDto getTeachersByWord(String word) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = prefix + TEACHER_URI + "/word";
        HttpHeaders headers = new HttpHeaders();
        List<Charset> charsets = new ArrayList<>();
        charsets.add(Charsets.UTF_8);
        headers.setAcceptCharset(charsets);
        HttpEntity<String> request = new HttpEntity<>(word, headers);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        //https://stackoverflow.com/questions/19540289/how-to-fix-the-java-security-cert-certificateexception-no-subject-alternative
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);

        return restTemplate.postForObject(uri, request, TeacherListDto.class);
    }

    public TeacherFullTimeLessonDto getTeacherLessonsByDay(String teacherId, String day) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = prefix + TEACHER_URI + "/" + teacherId + "/" + day;

        //https://stackoverflow.com/questions/19540289/how-to-fix-the-java-security-cert-certificateexception-no-subject-alternative
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);

        return restTemplate.getForObject(uri, TeacherFullTimeLessonDto.class);
    }

    public TeacherExamPeriodEventDto getTeacherExamPeriodEvents(String teacherId) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = prefix + TEACHER_URI + "/" + teacherId + "/exam";

        //https://stackoverflow.com/questions/19540289/how-to-fix-the-java-security-cert-certificateexception-no-subject-alternative
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);

        return restTemplate.getForObject(uri, TeacherExamPeriodEventDto.class);
    }

    public ExtramuralDto getExtramuralEventsByGroup(String departmentUrl,
                                                    String groupNumber) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = prefix +
                String.format(
                        EXTR_EV_GROUP_URI,
                        departmentUrl,
                        groupNumber
                );

        //https://stackoverflow.com/questions/19540289/how-to-fix-the-java-security-cert-certificateexception-no-subject-alternative
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);

        return restTemplate.getForObject(uri, ExtramuralDto.class);
    }

    public ExtramuralDto getExtramuralEventsByDay(String departmentUrl,
                                                  String groupNumber,
                                                  Integer month,
                                                  Integer day) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = prefix +
                String.format(
                        EXTR_EV_GROUP_URI_MONTH_DAY,
                        departmentUrl,
                        groupNumber,
                        month.toString(),
                        day.toString()
                );

        //https://stackoverflow.com/questions/19540289/how-to-fix-the-java-security-cert-certificateexception-no-subject-alternative
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);

        return restTemplate.getForObject(uri, ExtramuralDto.class);
    }
}
