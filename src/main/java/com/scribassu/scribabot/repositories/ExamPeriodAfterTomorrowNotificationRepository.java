package com.scribassu.scribabot.repositories;

import com.scribassu.scribabot.entities.ExamPeriodAfterTomorrowNotification;
import com.scribassu.scribabot.entities.ExamPeriodTomorrowNotification;
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

    @Query("select s from ExamPeriodAfterTomorrowNotification s where s.userId = :userId")
    ExamPeriodAfterTomorrowNotification findByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("update ExamPeriodAfterTomorrowNotification set isEnabled = true where userId = :userId")
    void enableByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("update ExamPeriodAfterTomorrowNotification set isEnabled = false where userId = :userId")
    void disableByUserId(@Param("userId") String userId);
}
