package com.scribassu.scribabot.repositories.notifications;

import com.scribassu.scribabot.entities.notifications.ExamPeriodAfterTomorrowNotification;
import com.scribassu.scribabot.model.BotUserSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ExamPeriodAfterTomorrowNotificationRepository extends JpaRepository<ExamPeriodAfterTomorrowNotification, Long> {

    @Query("select s from ExamPeriodAfterTomorrowNotification s where s.hourForSend = :hour and s.isEnabled = true")
    List<ExamPeriodAfterTomorrowNotification> findByHourForSendAndEnabled(@Param("hour") int hour);

    @Query("select s from ExamPeriodAfterTomorrowNotification s where s.userId = :userId and s.userSource = :userSource")
    ExamPeriodAfterTomorrowNotification findByUserIdAndUserSource(@Param("userId") String userId,
                                                                  @Param("userSource") BotUserSource userSource);

    @Modifying
    @Transactional
    @Query("update ExamPeriodAfterTomorrowNotification set isEnabled = true where userId = :userId and userSource = :userSource")
    void enableByUserIdAndUserSource(@Param("userId") String userId,
                                     @Param("userSource") BotUserSource userSource);

    @Modifying
    @Transactional
    @Query("update ExamPeriodAfterTomorrowNotification set isEnabled = false where userId = :userId and userSource = :userSource")
    void disableByUserIdAndUserSource(@Param("userId") String userId,
                                      @Param("userSource") BotUserSource userSource);

    @Modifying
    @Transactional
    @Query("delete from ExamPeriodAfterTomorrowNotification where userId = :userId and userSource = :userSource")
    void deleteByUserIdAndUserSource(@Param("userId") String userId,
                                     @Param("userSource") BotUserSource userSource);
}
