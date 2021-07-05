package com.scribassu.scribabot.repositories;

import com.scribassu.scribabot.entities.ExamPeriodTodayNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ExamPeriodTodayNotificationRepository extends JpaRepository<ExamPeriodTodayNotification, Long> {

    @Query("select s from ExamPeriodTodayNotification s where s.hourForSend = :hour and s.isEnabled = true")
    List<ExamPeriodTodayNotification> findByHourForSendAndEnabled(@Param("hour") int hour);

    @Query("select s from ExamPeriodTodayNotification s where s.userId = :userId")
    ExamPeriodTodayNotification findByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("update ExamPeriodTodayNotification set isEnabled = true where userId = :userId")
    void enableByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("update ExamPeriodTodayNotification set isEnabled = false where userId = :userId")
    void disableByUserId(@Param("userId") String userId);
}
