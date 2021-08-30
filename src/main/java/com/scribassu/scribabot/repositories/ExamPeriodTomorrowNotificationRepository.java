package com.scribassu.scribabot.repositories;

import com.scribassu.scribabot.entities.ExamPeriodTomorrowNotification;
import com.scribassu.scribabot.util.BotUserSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ExamPeriodTomorrowNotificationRepository extends JpaRepository<ExamPeriodTomorrowNotification, Long> {

    @Query("select s from ExamPeriodTomorrowNotification s where s.hourForSend = :hour and s.isEnabled = true")
    List<ExamPeriodTomorrowNotification> findByHourForSendAndEnabled(@Param("hour") int hour);

    @Query("select s from ExamPeriodTomorrowNotification s where s.userId = :userId and s.userSource = :userSource")
    ExamPeriodTomorrowNotification findByUserIdAndUserSource(@Param("userId") String userId,
                                                             @Param("userSource") BotUserSource userSource);

    @Modifying
    @Transactional
    @Query("update ExamPeriodTomorrowNotification set isEnabled = true where userId = :userId and userSource = :userSource")
    void enableByUserIdAndUserSource(@Param("userId") String userId,
                                     @Param("userSource") BotUserSource userSource);

    @Modifying
    @Transactional
    @Query("update ExamPeriodTomorrowNotification set isEnabled = false where userId = :userId and userSource = :userSource")
    void disableByUserIdAndUserSource(@Param("userId") String userId,
                                      @Param("userSource") BotUserSource userSource);

    @Modifying
    @Transactional
    @Query("delete from ExamPeriodTomorrowNotification where userId = :userId and userSource = :userSource")
    void deleteByUserIdAndUserSource(@Param("userId") String userId,
                                     @Param("userSource") BotUserSource userSource);
}
