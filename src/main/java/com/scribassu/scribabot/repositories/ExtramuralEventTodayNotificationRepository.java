package com.scribassu.scribabot.repositories;

import com.scribassu.scribabot.entities.ExtramuralEventTodayNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ExtramuralEventTodayNotificationRepository extends JpaRepository<ExtramuralEventTodayNotification, Long> {

    @Query("select s from ExtramuralEventTodayNotification s where s.hourForSend = :hour and s.isEnabled = true")
    List<ExtramuralEventTodayNotification> findByHourForSendAndEnabled(@Param("hour") int hour);

    @Query("select s from ExtramuralEventTodayNotification s where s.userId = :userId")
    ExtramuralEventTodayNotification findByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("update ExtramuralEventTodayNotification set isEnabled = true where userId = :userId")
    void enableByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("update ExtramuralEventTodayNotification set isEnabled = false where userId = :userId")
    void disableByUserId(@Param("userId") String userId);
}
