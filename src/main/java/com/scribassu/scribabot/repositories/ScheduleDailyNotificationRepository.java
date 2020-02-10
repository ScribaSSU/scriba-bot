package com.scribassu.scribabot.repositories;

import com.scribassu.scribabot.entities.ScheduleDailyNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ScheduleDailyNotificationRepository extends JpaRepository<ScheduleDailyNotification, Long> {

    @Query("select s from ScheduleDailyNotification s where s.hourForSend = :hour and s.isEnabled = true")
    List<ScheduleDailyNotification> findByHourForSendAndEnabled(@Param("hour") int hour);

    @Query("select s from ScheduleDailyNotification s where s.userId = :userId")
    ScheduleDailyNotification findByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("update ScheduleDailyNotification set isEnabled = true where userId = :userId")
    void enableScheduleDailyNotificationByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("update ScheduleDailyNotification set isEnabled = false where userId = :userId")
    void disableScheduleDailyNotificationByUserId(@Param("userId") String userId);
}
